${package}

public interface ${className} {

    ${type} ${name}(<#list fields as field>${field.type} ${field.name}<#if field_has_next>, </#if></#list>);

}