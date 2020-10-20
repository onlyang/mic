package xn;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class MegerResultItem {

    private String name;
    private Map<Integer,MegerSummer> info = new TreeMap<>();


}
