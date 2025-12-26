package study.data_jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.data_jpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {
	private Long id;
	private String username;
	private String teamName;

	public MemberDto(Member member) {
		this.id = member.getId();
		this.username = member.getUsername();
		if (member.getTeam() != null) {
			this.teamName = member.getTeam().getName();
		}
	}
}
