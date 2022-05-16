package net.getko.iilrepository.components;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Domain-DTO Object Mapper Component.
 *
 * @param <F>   The FROM object type
 * @param <T>   The TO object type
 * @author Nikolaos Vastardis (email: Nikolaos.Vastardis@gla-rad.org)
 */
@Component
public class DomainDtoMapper<F, T> {

    /**
     * The Model Mapper.
     */
    @Autowired
    ModelMapper modelMapper;

    /**
     * Returns the model mapper.
     *
     * @return the model mapper
     */
    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    /**
     * Map the FROM object page into a TO page.
     *
     * @param page              The FROM object page
     * @param clazz             The class to map the FROM objects to
     * @return the mapped TO object page
     */
    @Transactional
    public Page<T> convertToPage(Page<F> page, Class<T> clazz) {
        return page.map(obj -> this.convertTo(obj, clazz));
    }

    /**
     * Map the FROM object list into a TO list.
     *
     * @param list              The FROM object list
     * @param clazz             The class to map the FROM objects to
     * @return the mapped TO object list
     */
    @Transactional
    public List<T> convertToList(List<F> list, Class<T> clazz) {
        return list.stream()
                .map(obj -> this.convertTo(obj, clazz))
                .collect(Collectors.toList());
    }

    /**
     * Map the FROM object into a TO object.
     *
     * @param fromObj           The FROM object
     * @param clazz             The class to map the FROM object to
     * @return the mapped TO object
     */
    @Transactional
    public T convertTo(F fromObj, Class<T> clazz) {
        return this.modelMapper.map(fromObj, clazz);
    }

}
