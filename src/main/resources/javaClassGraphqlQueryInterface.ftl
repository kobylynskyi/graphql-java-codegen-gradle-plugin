public interface ${className} {

    ${returnType} ${name}(<#list params as param> ${param.getType().getName()} ${param.name}</#list>)

}