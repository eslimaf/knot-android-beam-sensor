package br.org.cesar.knot.beamsensor.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by carlos on 14/03/17.
 */

public class BeamSensorFilter {

    public enum FilterLinkType {
        And,Or
    }

    public enum FilterCompareValueMode {
        Equal,GreatThan,LessThan,In,Like
    }

    private List<FilterItem> items;

    private class FilterItem{

        private Map<String,?> keyValueMap;
        private FilterLinkType siblingLinkType;
        private FilterCompareValueMode compareValueMode;
        private FilterItem sibling;

        public Map<String, ?> getKeyValueMap() {
            return keyValueMap;
        }

        public void setKeyValueMap(Map<String, ?> keyValueMap) {
            this.keyValueMap = keyValueMap;
        }

        public FilterCompareValueMode getCompareValueMode() {
            return compareValueMode;
        }

        public void setCompareValueMode(FilterCompareValueMode compareValueMode) {
            this.compareValueMode = compareValueMode;
        }

        public FilterItem getSibling() {
            return sibling;
        }

        public void setSibling(FilterItem sibling,FilterLinkType siblingLinkType) {
            this.sibling = sibling;
            this.siblingLinkType = siblingLinkType;
        }

        public FilterLinkType getSiblingLinkType() {
            return siblingLinkType;
        }

    }

    public BeamSensorFilter(){
        items = new ArrayList<>();
    }

    public <T> void build(String name,T value,FilterCompareValueMode compareValueMode){
        FilterItem filterItem = new FilterItem();
        Map<String, T> map = new HashMap<String, T>();
        map.put(name,value);
        filterItem.setKeyValueMap(map);
        filterItem.setCompareValueMode(compareValueMode);
        items.add(filterItem);
    }

    public <T,Z> void build(String param1,T paramValue1,FilterCompareValueMode compareValueMode1,FilterLinkType siblingLinkType,
                            String param2,Z paramValue2,FilterCompareValueMode compareValueMode2){

        FilterItem filterItem = new FilterItem();
        Map<String, T> leftMap = new HashMap<>();
        leftMap.put(param1,paramValue1);
        filterItem.setKeyValueMap(leftMap);
        filterItem.setCompareValueMode(compareValueMode1);

        FilterItem filterItem1 = new FilterItem();
        Map<String, Z> rightMap = new HashMap<>();
        rightMap.put(param2,paramValue2);
        filterItem1.setKeyValueMap(rightMap);
        filterItem1.setCompareValueMode(compareValueMode2);

        filterItem.setSibling(filterItem1,siblingLinkType);
        items.add(filterItem);
    }

    public JSONObject getQuery() throws JSONException {
        JSONObject query = new JSONObject();
        JSONArray array = new JSONArray();
        for (FilterItem item : items) {
            FilterItem sibling = item.getSibling();
            if (sibling != null) {
                if (item.getSiblingLinkType() == FilterLinkType.Or) {
                    array.put(create(item));
                    array.put(create(item.getSibling()));
                    query.put("$or", array);
                }else if (item.getSiblingLinkType() == FilterLinkType.And) {
                    append(item,query);
                    append(sibling,query);
                }
            }
            else{
                append(item,query);
            }
        }
        return query;
    }

    private JSONObject create(FilterItem item){
        JSONObject query = new JSONObject();
        createComparativeValueObject(item, query);
        return query;
    }

    private void append(FilterItem item,JSONObject query){
        createComparativeValueObject(item, query);
    }

    private void createComparativeValueObject(FilterItem item, JSONObject query) {
        Set<? extends Map.Entry<String, ?>> entries = item.getKeyValueMap().entrySet();
        for (Map.Entry<String, ?> entry : entries) {
            try {
                if (item.getCompareValueMode() == FilterCompareValueMode.Equal) {
                    query.put(entry.getKey(), entry.getValue());
                }else if (item.getCompareValueMode() == FilterCompareValueMode.GreatThan) {
                    JSONObject greaterThan = new JSONObject();
                    greaterThan.put("$gt",entry.getValue());
                    query.put(entry.getKey(), greaterThan);
                }else if (item.getCompareValueMode() == FilterCompareValueMode.LessThan) {
                    JSONObject lessThan = new JSONObject();
                    lessThan.put("$lt",entry.getValue());
                    query.put(entry.getKey(), lessThan);
                }else if (item.getCompareValueMode() == FilterCompareValueMode.In) {
                    JSONObject in = new JSONObject();
                    JSONArray array = new JSONArray();
                    ArrayList list = (ArrayList) entry.getValue();
                    for (Object o : list) {
                        array.put(o);
                    }
                    in.put("$in", array);
                    query.put(entry.getKey(), in);
                }else if (item.getCompareValueMode() == FilterCompareValueMode.Like) {
                    query.put(entry.getKey(), "//ˆ"+ entry.getValue().toString()+ "//");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
