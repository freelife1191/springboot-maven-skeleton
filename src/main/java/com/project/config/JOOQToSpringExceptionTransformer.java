package com.project.config;


import org.jooq.impl.DefaultExecuteListener;

public class JOOQToSpringExceptionTransformer extends DefaultExecuteListener {

    /*
    @Override
    public void exception(ExecuteContext ctx) {
        SQLDialect dialect = ctx.configuration().dialect();
        SQLExceptionTranslator translator = (dialect != null)
                ? new SQLErrorCodeSQLExceptionTranslator(dialect.name())
                : new SQLStateSQLExceptionTranslator();

        ctx.exception(translator.translate("jOOQ", ctx.sql(), ctx.sqlException()));
    }
    */
}