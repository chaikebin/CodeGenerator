package ${packageName}.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

public class ${table.className}Dao {

    static final String TABLE = "${table.tableName}";
    static final String INSERT_FIELDS = "#foreach($column in $table.columns)${column.columnName},#end";
    static final String VALUE_FIELDS = "#foreach($column in $table.columns)#{${column.javaProperty}},#end";
    static final String QUERY_FIELDS= "#foreach($column in $table.columns)${column.columnName} as ${column.javaProperty},#end";
    static final String UPDATE_FIELDS = "#foreach($column in $table.columns)${column.columnName}=#{${column.javaProperty}},#end";
    
    @Insert("INSERT INTO " + TABLE + "(" + INSERT_FIELDS + ")VALUES(" + VALUE_FIELDS + ")")
    @SelectKey(before = false, statement = "SELECT LAST_INSERT_ID() AS id", keyProperty = "id", resultType = Long.class)
    void add(${table.className} ${table.className2});
    
    @Update("UPDATE " + TABLE + " SET " + UPDATE_FIELDS+ " WHERE id = #{id}")
    void update(${table.className} ${table.className2});
    
    @Delete("DELETE FROM " + TABLE + " where id = #{id}")
    void delete(@Param("id") Long id);
}