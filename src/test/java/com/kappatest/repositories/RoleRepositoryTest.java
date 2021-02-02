package com.kappatest.repositories;

import com.kappatest.TestConfig;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(excludeAutoConfiguration = {EmbeddedMongoAutoConfiguration.class})
//@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestConfig.class)
class RoleRepositoryTest {

//  @Autowired
//  private RoleRepository roleRepository;
//
//  @TestConfiguration
//  class userRepositoryTestConfig {
//
//    @Bean
//    public MongoRepositoryFactoryBean mongoFactoryRepositoryBean(MongoTemplate template) {
//      MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(
//          RoleRepository.class);
//      mongoDbFactoryBean.setMongoOperations(template);
//      return mongoDbFactoryBean;
//    }
//  }
//
//  @BeforeAll
//  void setUp() throws Exception {
//    Role roleAdmin = new Role("1", "ROLE_ADMIN");
//    Role roleUser = new Role("2", "ROLE_USER");
//    Set<Role> roles = new HashSet<>();
//    roles.add(roleAdmin);
//    roles.add(roleUser);
//    roleRepository.saveAll(roles);
//  }
//
//  @DisplayName("Test role list")
//  @Test
//  void whenFindAll_returnRoleList() {
//    List<Role> roleList = roleRepository.findAll();
//    assertThat(roleList.size()).isGreaterThanOrEqualTo(2);
//  }
//
//  @DisplayName("Test role detail")
//  @Test
//  void whenFindByAuthority_returnExactRole() {
//    Role roleUser = roleRepository.findByAuthority("ROLE_USER");
//    assertThat(roleUser).isNotNull();
//    assertThat(roleUser.getAuthority()).isEqualTo("ROLE_USER");
//  }
//
//  @DisplayName("Test save role")
//  @Test
//  void whenSaveRole_thenFoundInDB() {
//    Role roleDBA = new Role("3", "ROLE_DBA");
//    roleRepository.save(roleDBA);
//    Role found = roleRepository.findByAuthority("ROLE_DBA");
//    assertThat(found).isNotNull();
//    assertThat(found.getAuthority()).isEqualTo("ROLE_DBA");
//  }

}
