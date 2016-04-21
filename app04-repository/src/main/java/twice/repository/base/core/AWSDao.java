package twice.repository.base.core;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.apache.log4j.Category;
import utils.exception.dao.ConnectionNoFoundException;
import utils.exception.dao.DaoOperationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Gabriel on 12/04/2016.
 * Dao para acceder a la base de datos de Dynamo de Amazon Web Services
 */
public abstract class AWSDao<T> {
    private static AmazonDynamoDBClient dynamoDB;
    private List<KeySchemaElement> keySchema = new ArrayList();
    private List<AttributeDefinition> attributeList = new ArrayList();
    private String tableName = "N/A";
    private Category log = Category.getInstance("dao.debug");

    public AWSDao() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConnectionNoFoundException();
        }
    }

    private void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [TwicePixels]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\USERNAME\\.aws\\credentials).
         */
        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("TwicePixels").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (C:\\Users\\USERNAME\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
    }

    /**
     * Método para ligar el dao a una tabla para que se cree via aplicación
     * Si no se desea usar una tabla se deja como 'N/A'
     *
     * @return el nombre de la tabla
     */
    protected String getTableName() {
        return tableName;
    }

    protected void setTableName(String tableName) {
        this.tableName = tableName;
    }

    protected void createAllTable() {
        String tableName = getTableName();
        if (tableName != null && !tableName.equals("N/A")) {
            CreateTableRequest createTableRequest = new CreateTableRequest(attributeList, tableName, keySchema, new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
            TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
        }
    }

    protected void createOnlyTable() {
        String tableName = getTableName();
        if (tableName != null && !tableName.equals("N/A")) {
            //keySchema.add(new KeySchemaElement(, type.getType())); TODO
            CreateTableRequest createTableRequest = new CreateTableRequest(tableName, keySchema);
            TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
        }
    }

    private void addColumn(String columnName, Class c, FieldType type) {
        if (type.equals(FieldType.ATTRIBUTE)) {
            attributeList.add(new AttributeDefinition(columnName, getScalarAttributeType(c)));
        } else {
            keySchema.add(new KeySchemaElement(columnName, type.getType()));
        }
    }

    private ScalarAttributeType getScalarAttributeType(Class c) {
        if (c == Number.class) {
            return ScalarAttributeType.N;
        } else if (c == String.class) {
            return ScalarAttributeType.S;
        } else if (c == Boolean.class) {
            return ScalarAttributeType.B;
        } else {
            return null;
        }
    }

    /**
     * Método para insertar un registros
     *
     * @param addItem el item a incluir debe ser de la clase <T>
     * @return el <T> resultante
     * @throws DaoOperationException
     */
    public T add(T addItem) {
        //Setear el nombre de la tabla con la clase
        this.setTableName(addItem.getClass().getName());
        //Esto solo va a funcionar la primera vez si la tabla no existe pero luego el detecta que no es necesario crearla de nuevo
        //createOnlyTable(); TODO
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        mapper.save(addItem);
        return addItem;
    }

    /**
     * Método para actualizar un objeto en la base de datos
     *
     * @param updateItem el objeto a modificar
     * @return el objeto modificado
     */
    protected T update(T updateItem) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        mapper.save(updateItem);
        return updateItem;
    }

    /**
     * Método para borrar un Item de la base de datos
     *
     * @param deleteItem El objeto a borrar
     * @return
     */
    protected boolean delete(T deleteItem) {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
            mapper.delete(deleteItem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error to delete: ", e);
            return false;
        }
    }

    /**
     * Método para obtener un T Object de la base de datos
     *
     * @param filters los filtros de la consulta
     * @return la lista de datos seteados en los pojos
     */
    protected List<T> getList(Map filters, Class<T> c) throws DaoOperationException {
        try {
            if (filters != null && filters.size() > 0) {
                HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
                //Setear el nombre de la tabla con la clase
                String tableName = c.getSimpleName();
                Iterator iterator = filters.keySet().iterator();
                while (iterator.hasNext()) {
                    Object key = iterator.next();
                    Object value = filters.get(key);
                    AttributeValue consultValue;
                    if (value instanceof Number) {
                        consultValue = new AttributeValue().withN(value.toString());
                    } else {
                        consultValue = new AttributeValue().withS(value.toString());
                    }
                    Condition condition = new Condition()
                            .withComparisonOperator(ComparisonOperator.EQ)
                            .withAttributeValueList(consultValue);
                    scanFilter.put(key.toString(), condition);
                }
                ScanRequest scanRequest = new ScanRequest(tableName.toUpperCase()).withScanFilter(scanFilter);
                ScanResult scanResult = dynamoDB.scan(scanRequest);
                List<T> result = new ArrayList<>();
                for (Map<String, AttributeValue> resulMap : scanResult.getItems()) {
                    Object o = c.newInstance();
                    Iterator iteratorResult = resulMap.keySet().iterator();
                    //setear valores
                    while (iteratorResult.hasNext()) {
                        Object key = iteratorResult.next();
                        AttributeValue value = resulMap.get(key);
                        Object valueObject;
                        if (value.isBOOL() != null && value.isBOOL()) {
                            valueObject = value.getBOOL();
                        } else if (value.isNULL() != null && value.isNULL()) {
                            valueObject = value.getNULL();
                        } else if (value.getS() != null) {
                            valueObject = value.getS();
                        } else {
                            valueObject = value.getN();
                        }
                        Method[] methods = c.getMethods();
                        boolean find = false;
                        String methodName = "";
                        int i = 0;
                        while (!find && i < methods.length) {
                            if (methods[i].getName().equalsIgnoreCase("set" + key)) {
                                methodName = methods[i].getName();
                                find = true;
                            } else {
                                i++;
                            }
                        }

                        Method method1 = c.getDeclaredMethod(methodName, new Class[]{valueObject.getClass()});
                        Object[] parameters = new Object[]{valueObject};
                        method1.invoke(o, parameters);
                    }
                    result.add((T) o);
                }
                return result;
            } else {
                return null;
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            log.error("Error to consult: ", e);
            throw new DaoOperationException("Error getList : " + c.getName() + " params" + filters.toString());
        }
    }

    /**
     * Método para obtener un resgistro del tipo T
     *
     * @param filters los datos a filtrar
     * @param c       el tipo del dato
     * @return el objeto T solicitado
     * @throws DaoOperationException
     */
    protected T get(Map filters, Class<T> c) throws DaoOperationException {
        List<T> resultList = getList(filters, c);
        if (resultList != null && resultList.size() > 0) {
            return resultList.get(0);
        } else {
            return null;
        }
    }

    /**
     * M[etodo para buscar un objeto por la llave primaria
     * @param searchItem el typo de objecto a buscar y que contenga los datos de @DynamoDBHashKey
     * @return el objecto ya con los datos cargados de la base de datos
     */
    protected T getItemByKey(T searchItem) {
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        return mapper.load(searchItem);
    }

}
