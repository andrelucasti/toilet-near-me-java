package io.andrelucas.toiletnearme;

import io.andrelucas.toiletnearme.owner.infrastructure.jpa.OwnerSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClearData {

    @Autowired
    private ToiletSpringRepository toiletSpringRepository;
    @Autowired
    private ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    @Autowired
    private OwnerSpringRepository ownerSpringRepository;

    public void clear() {
        ownerSpringRepository.deleteAll();
        toiletSpringRepository.deleteAll();
        toiletOutboxSpringRepository.deleteAll();
    }
}
