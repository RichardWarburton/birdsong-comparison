package com.insightfullogic.birdsong.spark_in_memory;

import com.insightfullogic.birdsong.ServiceRule;
import com.insightfullogic.birdsong.SingingSpec;
import com.insightfullogic.birdsong.UserSpec;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserSpec.class, SingingSpec.class})
public class SpecificationSuite {
    @BeforeClass
    public static void setup() {
        ServiceRule.setService(new SparkApplicationRunner());
    }
}
