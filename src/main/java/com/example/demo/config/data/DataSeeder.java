//package com.example.demo.config.data;
//
//
//import com.example.demo.model.domain.job.Company;
//import com.example.demo.model.domain.job.CompanyLogo;
//import com.example.demo.model.domain.job.ExperienceLevel;
//import com.example.demo.model.domain.job.Job;
//import com.example.demo.model.domain.subscriber.Skill;
//import com.example.demo.model.domain.subscriber.Subscriber;
//import com.example.demo.model.domain.user.User;
//import com.example.demo.repository.*;
//import com.github.javafaker.Faker;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.stream.IntStream;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder implements CommandLineRunner {
//    private final UserRepository userRepository;
//    private final CompanyRepository companyRepository;
//    private final JobRepository jobRepository;
//    private final SubscriberRepository subscriberRepository;
//    private final SkillRepository skillRepository;
//    private final Faker faker = new Faker(Locale.US);
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        List<User> users = userRepository.saveAll(seedUserData());
//        subscriberRepository.saveAll(seedSubscriberData(users));
//
//        List<Company> companies = companyRepository.saveAll(seedCompanyData());
//        jobRepository.saveAll(seedJobData(companies));
//    }
//
//    public List<User> seedUserData(){
//        return IntStream.range(0, 1000)
//                .mapToObj(i -> User.builder()
//                        .fullName(faker.name().fullName())
//                        .email(faker.internet().emailAddress())
//                        .address(faker.address().fullAddress())
//                        .build())
//                .toList();
//    }
//
//    public List<Subscriber> seedSubscriberData(List<User> users){
//        List<Skill> skills = skillRepository.findAll();
//        ExperienceLevel[] levels = ExperienceLevel.values();
//        return users
//                .stream()
//                .map((u) -> {
//                    List<Skill> subList = new ArrayList<>();
//                    for(int j = 0 ; j < 3; j++){
//                        int skillID = ThreadLocalRandom.current().nextInt(skills.size());
//                        subList.add(skills.get(skillID));
//                    }
//                    return Subscriber.builder()
//                            .level(levels[ThreadLocalRandom.current().nextInt(levels.length)])
//                            .email(u.getEmail())
//                            .expectedSalary(Double.parseDouble(faker.commerce().price(1000000.0,3000000.0).replace(",", ".")))
//                            .skills(subList)
//                            .build();
//                })
//                .toList();
//    }
//
//
//    public List<Job> seedJobData(List<Company> companies){
//        List<Skill> skills = skillRepository.findAll();
//        ExperienceLevel[] levels = ExperienceLevel.values();
//        return IntStream.range(0, 2000)
//                .mapToObj(i -> {
//                    int companyId = ThreadLocalRandom.current().nextInt(companies.size());
//                    List<Skill> subList = new ArrayList<>();
//                    for(int j = 0 ; j < 5; j++){
//                        int skillID = ThreadLocalRandom.current().nextInt(skills.size());
//                        subList.add(skills.get(skillID));
//                    }
//
//                    return Job.builder()
//                            .name(faker.job().title())
//                            .active(true)
//                            .salary(Double.parseDouble(faker.commerce().price(1000000.0,3000000.0).replace(",", ".")))
//                            .quantity(3)
//                            .location(faker.address().fullAddress())
//                            .skills(subList)
//                            .level(levels[ThreadLocalRandom.current().nextInt(levels.length)])
//                            .company(companies.get(companyId))
//                            .build();
//                })
//                .toList();
//    }
//
//    public List<Company> seedCompanyData(){
//        return IntStream.range(0, 1000)
//                .mapToObj(i -> Company.builder()
//                        .name(faker.company().name())
//                        .logo(CompanyLogo.builder()
//                                .fileUrl(faker.company().logo())
//                                .build())
//                        .description(faker.company().buzzword())
//                        .build())
//                .toList();
//    }
//}
