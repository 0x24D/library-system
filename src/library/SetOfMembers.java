/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.util.ArrayList;

/**
 * A set of members in the library.
 *
 * @author Kutoma
 * @author Redacted
 */
public class SetOfMembers extends ArrayList<Member> {

    public void addMember(Member aMember) {
        super.add(aMember);

    }

    public Member getMemberFromName(String name) {
        return super.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
    }

    public Member getMemberFromNumber(int number) {
        return super.stream().filter(m -> m.getNumber() == number).findFirst().orElse(null);
    }

    public void returnMember(Member member) {
        super.remove(member);
    }

}
