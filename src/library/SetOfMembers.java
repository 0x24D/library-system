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
        return null;
    }

    public Member getMemberFromNumber(int number) {
        return null;
    }

    public void returnMember(Member member) {
    }

}
