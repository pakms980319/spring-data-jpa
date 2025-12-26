package study.data_jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;

	@Test
	public void testMember() {
		Member member = new Member("memberA");

		Member savedMember = memberRepository.save(member);

		Member findMember = memberRepository.findById(savedMember.getId()).get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberRepository.save(member1);
		memberRepository.save(member2);

		// 단건 조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		// 리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		// 카운트 검증
		long count = memberRepository.count();
		assertThat(count).isEqualTo(2);

		// 삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> findMember = memberRepository.findByUsername(m1.getUsername());

		assertThat(findMember.get(0).getUsername()).isEqualTo(m1.getUsername());
	}

	@Test
	public void testFindUser() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> findMember = memberRepository.findUser(m1.getUsername(), m1.getAge());

		assertThat(findMember.get(0).getUsername()).isEqualTo(m1.getUsername());
		assertThat(findMember.get(0).getAge()).isEqualTo(m1.getAge());
	}

	@Test
	public void testFindUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<String> usernameList = memberRepository.findUsernameList();

		for (String s : usernameList) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void testMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.changeTeam(team);
		memberRepository.save(m1);

		List<MemberDto> memberDtos = memberRepository.findMemberDto();

		for (MemberDto memberDto : memberDtos) {
			System.out.println("dto = " + memberDto);
		}
	}

	@Test
	public void testFindByNames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> findMembers = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

		for (Member findMember : findMembers) {
			System.out.println("findMember: " + findMember);
		}
	}

	@Test
	public void testReturnType() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> memberList = memberRepository.findListByUsername("AAA");
		Member member = memberRepository.findMemberByUsername("AAA");
		Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");

		System.out.println("====== member list ======");
		for (Member m : memberList) {
			System.out.println("m : " + m);
		}
		System.out.println("====== member list ======");
		System.out.println("member : " + member);
		System.out.println("optional member : " + optional.get());
	}
}