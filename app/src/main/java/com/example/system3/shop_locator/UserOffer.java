package com.example.system3.shop_locator;

import java.io.File;

public class UserOffer
        {
            public String Title ;
            public File ImagePath ;
            public String Description ;
            public int UserID ;
            public int LocationID ;


            public UserOffer(String title, File imagePath, String description, int userID, int locationID) {
                Title = title;
                ImagePath = imagePath;
                Description = description;
                UserID = userID;
                LocationID = locationID;
            }



        }