package org.drools.mapreduce.core.impl;

import org.drools.mapreduce.core.api.Collector;
import org.drools.mapreduce.core.api.Mapper;
import org.drools.mapreduce.core.model.entry.Entry;
import org.drools.mapreduce.core.util.DroolsHelper;
import org.kie.api.runtime.KieSession;

public class DroolsMapper implements Mapper<String, String, String, String>{
	private final String kieBase;
	
	public DroolsMapper() {
		this(null);
	}
	
	public DroolsMapper(String kieBase) {
		this.kieBase = kieBase;
	}

    public void map(String key, String value, Collector<String, String> collector) {
        KieSession kieSession = DroolsHelper.createKieSession(this.kieBase);
        kieSession.setGlobal("collector", collector);
        Entry entry = new Entry(key.toString(), value.toString());
        kieSession.insert(entry);
        kieSession.getAgenda().getAgendaGroup("mapper-rules").setFocus();
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
