package com.example.clockIn.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MyController {

    @Autowired
    private FilePlace filePlace;

    @RequestMapping("/loginIn")
    public String login(String name, String password){
    if(!StringUtils.isEmpty(name) && !StringUtils.isEmpty(password)){
        if(name.equals("徐梁") && password.equals("xuliang136543")){
            return "redirect:/login";
        }
    }
    return "redirect:/again";
    }

    @RequestMapping("/again")
    public String noIn(Model model){
        model.addAttribute("msg","信息验证失败");
        return "index";
    }

    @RequestMapping("/login")
    public String loginIn(Model model){
        String place = filePlace.getPlace();
        String time = read(place,1);
        model.addAttribute("time",StringUtils.isEmpty(time)? "?" : time);
        model.addAttribute("place",place);
        return "login";
    }

    @RequestMapping("/clock")
    @ResponseBody
    public String clock(@RequestBody String time){
        String result = "yes";
        if(!StringUtils.isEmpty(time)){
            String place = filePlace.getPlace();
            time = time.split("=")[1];

            String exitDate = read(place,2);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String sDay = sdf.format(new Date());

            //若时间缺失，那么直接写入
            if(StringUtils.isEmpty(exitDate)){
                setTime(place,(Integer.parseInt(time) +1) + "",sDay);
            }else{
                if(sDay.equals(exitDate)){
                    result = "no";
                }else{
                    setTime(place,(Integer.parseInt(time) +1) + "",sDay);
                }
            }
        }else{
            result = "noData";
        }
        return "{\"isOk\":\""+result+"\"}";
    }

    public String read(String place,int line){
        String time = "";
        BufferedReader bufr = null;
        try {
            FileReader fr2 = new FileReader(place);
            bufr = new BufferedReader(fr2);
//            String line = null;
//            while((line = bufr.readLine())!=null) {
//            }
            if(line == 1){
                time = bufr.readLine();
            }else{
                bufr.readLine();
                time = bufr.readLine();
            }

        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufr!=null)
                    bufr.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    public void setTime(String place,String time,String date){
        BufferedWriter bufw = null;
        try {
            FileWriter fr2 = new FileWriter(place);
            bufw = new BufferedWriter(fr2);
            bufw.write(time);
            bufw.newLine();
            bufw.write(date);
            bufw.flush();
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufw!=null)
                    bufw.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
