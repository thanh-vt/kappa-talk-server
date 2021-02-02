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
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestConfig.class)
class UserRepositoryTest {

//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private RoleRepository roleRepository;
//
//  @TestConfiguration
//  class userRepositoryTestConfig {
//
//    @Bean
//    public MongoRepositoryFactoryBean mongoFactoryRepositoryBean(MongoTemplate template) {
//      MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(
//          UserRepository.class);
//      mongoDbFactoryBean.setMongoOperations(template);
//
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
//    User admin = new User("1", "admin", "zero123456", roles, true, true, true, true);
//    userRepository.save(admin);
//    roles.add(roleUser);
//    roleRepository.saveAll(roles);
//    User user = new User("2", "user", "zero123456", roles, true, true, true, true);
//    userRepository.save(user);
//  }
//
//  @DisplayName("Test user list")
//  @Test
//  void whenFindAll_returnListOfUsers() {
//    List<User> userList = userRepository.findAll();
//    assertThat(userList.size()).isGreaterThanOrEqualTo(2);
//  }
//
//  @DisplayName("Test user detail")
//  @Test
//  void whenFindById_returnExactUser() {
//    Optional<User> user = userRepository.findById("2");
//    assertThat(user.isPresent()).isTrue();
//    user.ifPresent(value -> assertThat(value.getId()).isEqualTo("2"));
//  }
//
//  @DisplayName("Test save user")
//  @Test
//  void whenSave_thenFoundInDB() {
//    Role roleUser = roleRepository.findByAuthority("ROLE_USER");
//    Set<Role> roles = new HashSet<>();
//    roles.add(roleUser);
//    User pysga1996 = new User("3", "pysga1996", "zero123456", roles, true, true, true, true);
//    userRepository.save(pysga1996);
//    Optional<User> found = userRepository.findByUsername("pysga1996");
//    assertThat(found.isPresent()).isTrue();
//    found.ifPresent(value -> assertThat(value.getId()).isEqualTo("3"));
//    found.ifPresent(value -> assertThat(value.getUsername()).isEqualTo("pysga1996"));
//    found.ifPresent(value -> assertThat(value.getAuthorities()).contains(roleUser));
//  }


}
