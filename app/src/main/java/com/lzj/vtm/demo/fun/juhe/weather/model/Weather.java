package com.lzj.vtm.demo.fun.juhe.weather.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class Weather implements Serializable {

    public Realtime realtime;
    public Life life;
    public Pm25 pm25;
    public ArrayList<W_Weather> weather;

    public class Realtime{
        public String city_code;
        public String city_name;
        public String date;
        public String time;
        public int week;
        public String moon;
        public R_Weather weather;
        public R_Wind wind;

        public class R_Weather{
            public String temperature;
            public String humidity;
            public String info;
            public String img;
        }
        public class R_Wind{
            public String direct;
            public String power;
            public String offset;
            public String windspeed;
        }
    }

    public class Life{
        public String date;
        public L_Info info;
        public class L_Info{
            public String[] chuanyi;
            public String[] ganmao;
            public String[] kongtiao;
            public String[] wuran;
            public String[] xiche;
            public String[] yundong;
            public String[] ziwaixian;
        }
    }
    public class Pm25{
        public String key;
        public int show_desc;
        public P_Pm25 pm25;
        public String dateTime;
        public String cityName;
        public class P_Pm25{
            public String curPm;
            public int level;
            public String pm25;
            public String pm10;
            public String quality;
            public String des;
        }
    }
    public class W_Weather{

    }
}
