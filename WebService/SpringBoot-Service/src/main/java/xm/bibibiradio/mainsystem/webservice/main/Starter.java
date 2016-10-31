package xm.bibibiradio.mainsystem.webservice.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import xm.bibibiradio.mainsystem.webservice.controller.AuthorScoreController;
import xm.bibibiradio.mainsystem.webservice.controller.HelloController;
import xm.bibibiradio.mainsystem.webservice.controller.HelloRestfulController;
import xm.bibibiradio.mainsystem.webservice.controller.ResourceScoreController;
import xm.bibibiradio.mainsystem.webservice.controller.ResourceScoreRestfulController;
import xm.bibibiradio.mainsystem.webservice.controller.ViewerScoreController;

@SpringBootApplication
@EnableAutoConfiguration(exclude=DataSourceAutoConfiguration.class)
public class Starter {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Object[] controllerList = {Starter.class,HelloController.class
                                   ,ResourceScoreController.class,AuthorScoreController.class,
                                   ViewerScoreController.class,ResourceScoreRestfulController.class,
                                   HelloRestfulController.class};
        SpringApplication.run(controllerList, args);
    }

}
