package ${packageName}.model;

#if ($table.hasDateColumn)
import java.util.Date;
#end

public class ${table.className} {

#foreach($column in $table.columns)
    /** ${column.remarks} */
    private $column.javaType $column.javaProperty;
#end

#foreach($column in $table.columns)

    public ${column.javaType} ${column.getterMethodName}(){
        return this.${column.javaProperty};
    }
    public void ${column.setterMethodName}(${column.javaType} ${column.javaProperty}){
        this.${column.javaProperty} = ${column.javaProperty};
    }
#end
}