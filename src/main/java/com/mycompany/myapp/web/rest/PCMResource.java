package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.PCM;
import com.mycompany.myapp.repository.PCMRepository;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PCM.
 */
@RestController
@RequestMapping("/api")
public class PCMResource {

    private final Logger log = LoggerFactory.getLogger(PCMResource.class);

    @Inject
    private PCMRepository pCMRepository;

    /**
     * POST  /pCMs -> Create a new pCM.
     */
    @RequestMapping(value = "/pCMs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody PCM pCM) throws URISyntaxException {
        log.debug("REST request to save PCM : {}", pCM);
        if (pCM.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new pCM cannot already have an ID").build();
        }
        pCMRepository.save(pCM);
        return ResponseEntity.created(new URI("/api/pCMs/" + pCM.getId())).build();
    }

    /**
     * PUT  /pCMs -> Updates an existing pCM.
     */
    @RequestMapping(value = "/pCMs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody PCM pCM) throws URISyntaxException {
        log.debug("REST request to update PCM : {}", pCM);
        if (pCM.getId() == null) {
            return create(pCM);
        }
        pCMRepository.save(pCM);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /pCMs -> get all the pCMs.
     */
    @RequestMapping(value = "/pCMs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PCM>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<PCM> page = pCMRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pCMs", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pCMs/:id -> get the "id" pCM.
     */
    @RequestMapping(value = "/pCMs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PCM> get(@PathVariable Long id) {
        log.debug("REST request to get PCM : {}", id);
        return Optional.ofNullable(pCMRepository.findOne(id))
            .map(pCM -> new ResponseEntity<>(
                pCM,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pCMs/:id -> delete the "id" pCM.
     */
    @RequestMapping(value = "/pCMs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PCM : {}", id);
        pCMRepository.delete(id);
    }
}
