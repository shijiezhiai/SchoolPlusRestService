package com.websystique.springmvc.service.implementation;

import java.util.List;
import com.websystique.springmvc.model.Advertisement;
import com.websystique.springmvc.repository.AdvertisementRepository;
import com.websystique.springmvc.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangyma on 11/25/16.
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    AdvertisementRepository advertisementRepository;


    @Override
    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    @Override
    public Advertisement findById(Long id) {
        return advertisementRepository.findOne(id);
    }

    @Override
    public List<Advertisement> findByType(String type) {
        return advertisementRepository.findByType(type);
    }

    @Override
    public List<Advertisement> findByAdvertiser(String advertiser) {
        return advertisementRepository.findByAdvertiser(advertiser);
    }

    @Override
    public List<Advertisement> findByAdvertiserAndType(String advertiser, String type) {
        return advertisementRepository.findByAdvertiserAndType(advertiser, type);
    }

    @Override
    public Advertisement save(Advertisement ad) {
        return advertisementRepository.save(ad);
    }

    @Override
    public Boolean deleteById(Long adId) {
        try {
            advertisementRepository.delete(adId);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
