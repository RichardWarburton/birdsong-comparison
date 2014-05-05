package com.insightfullogic.spring_boot_impl;

import com.insightfullogic.birdsong.ServiceRule;
import com.insightfullogic.birdsong.SingingSpec;
import com.insightfullogic.birdsong.UserSpec;
import com.insightfullogic.spring_boot_impl.SpringBootApplicationRunner;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
//@Suite.SuiteClasses({UserSpec.class, SingingSpec.class})
@Suite.SuiteClasses({UserSpec.class})
public class SpecificationSuite {
    @BeforeClass
    public static void setup() {
        ServiceRule.setService(new SpringBootApplicationRunner());
    }
}
