package twice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twicebase.TwiceController;

@Controller
public class HomeController extends TwiceController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String viewApplication() {
        return "index";
    }
}