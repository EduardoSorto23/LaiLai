package net.dsdev.lailai.clientes.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.dsdev.lailai.clientes.model.Categories;
import net.dsdev.lailai.clientes.model.JsonMenus;
import net.dsdev.lailai.clientes.model.Menus;
import net.dsdev.lailai.clientes.model.SubCategories;
import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.model.menuDetail.OptionMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.VariantMenuDetail;
import net.dsdev.lailai.clientes.model.users.Client;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesMethods {

    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    Gson gson;
    String json;
    MenuDetailList menuDetailList;
    Activity activity;
    //prefsEditor = mPrefs.edit();
    public SharedPreferencesMethods(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public Double getFinalPrice(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        json = mPrefs.getString("shoppingCart", "");
        menuDetailList = gson.fromJson(json, MenuDetailList.class);
        Double finalPrice = 0.0;
        if (menuDetailList!=null && menuDetailList.getMenus()!=null && menuDetailList.getMenus().size()>0) {
            for (JsonMenuDetail menu : menuDetailList.getMenus()) {
                finalPrice += menu.getMenu().getFinalPrice();
            }
        }
        return finalPrice;
    }

    public Double getMenuExtraPrice(int position){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        json = mPrefs.getString("shoppingCart", "");
        menuDetailList = gson.fromJson(json, MenuDetailList.class);
        Double extraPrice = 0.0;
        if (menuDetailList!=null && menuDetailList.getMenus()!=null && menuDetailList.getMenus().size()>0){
            List<OptionMenuDetail> options = menuDetailList.getMenus().get(position).getMenu().getOptions();
            for (OptionMenuDetail option: options) {
                for (VariantMenuDetail variant: option.getVariants()) {
                    extraPrice += variant.getExtraPrice();
                }
            }
        }
        return extraPrice;
    }

    public JsonMenuDetail getMenuByPosition(int position){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        json = mPrefs.getString("shoppingCart", "");
        menuDetailList = gson.fromJson(json, MenuDetailList.class);

        return menuDetailList.getMenus().get(position);
    }

    public MenuDetailList getAllMenus(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        json = mPrefs.getString("shoppingCart", "");
        menuDetailList = gson.fromJson(json, MenuDetailList.class);

        return menuDetailList;
    }

    public void clearMenuDetailList(){
        prefsEditor = mPrefs.edit();
        prefsEditor.remove("shoppingCart");
        prefsEditor.apply();
    }

    public void saveMenuTree(String name, String value){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.remove(name);
        prefsEditor.putString(name,value);
        prefsEditor.apply();
    }

    public void deleteMenusTree(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        //json = mPrefs.getString("shoppingCart", "");
        prefsEditor.remove("shoppingCart");
        //json = mPrefs.getString("shoppingCart", "nulo");
        prefsEditor.apply();
    }

    public List<Menus> getMenusTree (String name){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        List<Menus> returnValue = new ArrayList<>();
        String menuJson = mPrefs.getString(name,"");
        JsonMenus jsonMenus;
        if (!menuJson.equals("")){
            jsonMenus = gson.fromJson(menuJson,JsonMenus.class);
            for (Categories cat : jsonMenus.getCategories()){
                Log.d("Hola", "getMenusTree: "+cat.getName());
                Menus headerMenu = new Menus();
                headerMenu.setName(cat.getName());
                headerMenu.setItemType(Constants.HEADER);
                returnValue.add(headerMenu);
                if (cat.getSubCategory().equalsIgnoreCase("si")){
                    for (SubCategories subCat : cat.getSubCategories()){
                        returnValue.addAll(subCat.getMenus());
                    }
                }else{
                    returnValue.addAll(cat.getMenus());
                }
            }
        }
        return returnValue;
    }

    public boolean isSweetSelected(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        return mPrefs.getBoolean("isSweetSelected",false);
    }

    public void saveSweetSelect(String key,boolean value){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(key,value);
        prefsEditor.apply();
    }

    public void saveLoggedUser(Client client){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString("loggedUser", new Gson().toJson(client));
        prefsEditor.apply();
    }

    public Client getLoggedUser(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        json = mPrefs.getString("loggedUser","");
        Client client = gson.fromJson(json, Client.class);
        return json.equals("") ? null : client;
    }

    public void deleteLoggedUser(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        prefsEditor.remove("loggedUser");
        prefsEditor.apply();
    }

    public void setIsLogged(boolean state){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("isLoggedIn",state);
        edit.apply();
    }

    public boolean getIsLogged(){
        mPrefs = activity.getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        return mPrefs.getBoolean("isLoggedIn",false);
    }
}
