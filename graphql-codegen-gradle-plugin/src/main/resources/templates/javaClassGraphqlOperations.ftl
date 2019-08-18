<#if package?has_content>
package ${package};

</#if>
public interface ${className} {

<#list operations as operation>
    ${operation.type} ${operation.name}(<#list operation.parameters as param>${param.type} ${param.name}<#if param_has_next>, </#if></#list>) throws Exception;

</#list>
}