package com.detection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.detection.model.user.CrUser;
import com.detection.model.user.UserRepository;

@Component
public class ApplicationInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;
    
    @Override
    public void run(String... arg0) throws Exception {
        // TODO Auto-generated method stub
        CrUser initUser = new CrUser();
        String username = null;
        String password = null;
        String role = null;
        if(arg0.length>0){
            for(String arg:arg0){
                if(arg.startsWith("--username")){
                    username = arg.split("=")[1];
                }
                else if(arg.startsWith("--password")){
                    password = arg.split("=")[1];
                }
                else if(arg.startsWith("--role")){
                    role = arg.split("=")[1];
                }
            }
        }
        else{
            initUser.setUserName("admin");
            initUser.setUserPassword("12345");
            initUser.setRole(1);
        }
        System.out.println(">>>>>>>>>>>>>>>>初始化完成...>>>>>>>>>>>>>");
    }

}
