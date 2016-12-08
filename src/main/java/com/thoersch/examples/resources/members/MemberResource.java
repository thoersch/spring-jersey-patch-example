package com.thoersch.examples.resources.members;

import com.thoersch.examples.init.interceptors.PATCH;
import com.thoersch.examples.persistence.members.MemberRepository;
import com.thoersch.examples.representations.members.Member;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class MemberResource {

    private MemberRepository memberRepo;

    @Inject
    public MemberResource(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @GET
    public List<Member> getAll() {
        return memberRepo.findAll();
    }

    @POST
    public Member post(Member member) {
        return memberRepo.save(member);
    }

    @GET
    @Path("/{id}")
    public Member get(@PathParam("id") long id) {
        return memberRepo.findOne(id);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        memberRepo.delete(id);
    }

    @PATCH
    @Path("/{id}")
    @Consumes("application/json-patch+json")
    public Member patch(Member member, @PathParam("id") long id) throws Exception {
        Member existingMember = memberRepo.findOne(id);
        if(existingMember == null) {
            throw new WebApplicationException("Member not found by id", Response.Status.NOT_FOUND);
        }

        new BeanUtilsBean() {
            @Override
            public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
                if(value == null) {
                    return;
                }
                super.copyProperty(bean, name, value);
            }
        }.copyProperties(existingMember, member);

        return memberRepo.save(existingMember);
    }
}
