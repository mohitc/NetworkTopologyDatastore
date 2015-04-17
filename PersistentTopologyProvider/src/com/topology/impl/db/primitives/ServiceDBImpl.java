package com.topology.impl.db.primitives;

import com.topology.primitives.Service;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "service")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ServiceDBImpl extends TopologyElementDBImpl implements Service{
}
