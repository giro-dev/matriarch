package dev.agiro.matriarch.object_samples;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AllArgsConstructorBasicObjectAllTypes {
    private String                             string;
    private Integer                            integer;
    private Boolean                            bool;
    private Double                             decimal;
    private Float                              floating;
    private Instant                            instant;
    private Long                               longInt;
    private Timestamp                          timestamp;
    private Character                          character;
    private BigDecimal                         bigDecimal;
    private java.util.UUID                     uuid;
    private java.time.LocalDate                date;
    private NoArgsConstructorBasicObject       nestedObject;
    private List<String>                       list;
    private List<NoArgsConstructorBasicObject> objectList;
    private Map<String, NoArgsConstructorBasicObject> map;


    public AllArgsConstructorBasicObjectAllTypes(String string,
                                                 int integer,
                                                 boolean bool,
                                                 double decimal,
                                                 float floating,
                                                 Instant instant,
                                                 long longInt,
                                                 Timestamp timestamp,
                                                 char character,
                                                 BigDecimal bigDecimal,
                                                 java.util.UUID uuid,
                                                 java.time.LocalDate date,
                                                 NoArgsConstructorBasicObject nestedObject,
                                                 List<String> list,
                                                 List<NoArgsConstructorBasicObject> objectList,
                                                 Map<String, NoArgsConstructorBasicObject> map) {
        this.string       = string;
        this.integer      = integer;
        this.bool         = bool;
        this.decimal      = decimal;
        this.floating     = floating;
        this.instant      = instant;
        this.longInt      = longInt;
        this.timestamp    = timestamp;
        this.character    = character;
        this.bigDecimal   = bigDecimal;
        this.uuid         = uuid;
        this.date         = date;
        this.nestedObject = nestedObject;
        this.list         = list;
        this.objectList   = objectList;
        this.map          = map;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public Boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public Double getDecimal() {
        return decimal;
    }

    public void setDecimal(double decimal) {
        this.decimal = decimal;
    }

    public Float getFloating() {
        return floating;
    }

    public void setFloating(float floating) {
        this.floating = floating;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public Long getLongInt() {
        return longInt;
    }

    public void setLongInt(long longInt) {
        this.longInt = longInt;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public NoArgsConstructorBasicObject getNestedObject() {
        return nestedObject;
    }

    public void setNestedObject(NoArgsConstructorBasicObject nestedObject) {
        this.nestedObject = nestedObject;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<NoArgsConstructorBasicObject> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<NoArgsConstructorBasicObject> objectList) {
        this.objectList = objectList;
    }

    public Map<String, NoArgsConstructorBasicObject> getMap() {
        return map;
    }

    public void setMap(Map<String, NoArgsConstructorBasicObject> map) {
        this.map = map;
    }
}
