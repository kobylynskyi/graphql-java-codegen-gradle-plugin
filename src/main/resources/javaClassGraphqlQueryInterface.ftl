public interface ${className} {

    ${returnType} ${name}(<#list params as param>${param.type} ${param.name}<#if param_has_next>, </#if></#list>)

}