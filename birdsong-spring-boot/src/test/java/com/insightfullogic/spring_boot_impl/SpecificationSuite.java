package com.insightfullogic.spring_boot_impl;

import com.insightfullogic.birdsong.ServiceRule;
import com.insightfullogic.birdsong.SingingSpec;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
//@Suite.SuiteClasses({UserSpec.class, SingingSpec.class})
//@Suite.SuiteClasses({UserSpec.class})
@Suite.SuiteClasses({SingingSpec.class})
public class SpecificationSuite {
    @BeforeClass
    public static void setup() {
        ServiceRule.setService(new SpringBootApplicationRunner());
    }
}
