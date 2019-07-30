${package}

public class ${className} {

<#list fields as field>
    ${field}<#if field_has_next>, </#if>
</#list>

}