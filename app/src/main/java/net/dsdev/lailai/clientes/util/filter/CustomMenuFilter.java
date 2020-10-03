package net.dsdev.lailai.clientes.util.filter;

import android.widget.Filter;

import net.dsdev.lailai.clientes.adapters.SearchAdapter;
import net.dsdev.lailai.clientes.model.Menus;
import net.dsdev.lailai.clientes.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomMenuFilter extends Filter {

    private SearchAdapter searchAdapter;
    private List<Menus> filterList;

    public CustomMenuFilter(SearchAdapter searchAdapter, List<Menus> filterList) {
        this.searchAdapter = searchAdapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        if (constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase();
            List<Menus> filteredMenus = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++){
                if (filteredMenus.size()>0 && (filterList.get(i).getItemType()== Constants.HEADER && filteredMenus.get(filteredMenus.size()-1).getItemType()== Constants.HEADER)){
                    filteredMenus.remove(filteredMenus.size()-1);
                }
                if (isContained(constraint.toString(), filterList.get(i).getName()) || (filterList.get(i).getItemType()== Constants.HEADER) ){
                    filteredMenus.add(filterList.get(i));
                }
            }
            if (filteredMenus.get(filteredMenus.size()-1).getItemType()==Constants.HEADER){
                filteredMenus.remove(filteredMenus.size()-1);
            }
            filterResults.count = filteredMenus.size();
            filterResults.values = filteredMenus;
        } else {
            filterResults.count = filterList.size();
            filterResults.values = filterList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        searchAdapter.setMenus((List<Menus>) results.values);
        searchAdapter.notifyDataSetChanged();
    }

    private boolean isContained(String constraint, String menuName){

        String[] words = constraint.split("\\s+");
        StringBuilder regex = new StringBuilder();
        for (String word:words) {
            regex.append("(?=.*").append(word).append(")");
        }
        Pattern pattern = Pattern.compile(regex.toString());

        return pattern.matcher(menuName).find();
    }

}
