package org.sopac.gem.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopac.gem.domain.Tool;
import org.sopac.gem.repository.ToolRepository;
import org.sopac.gem.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.sopac.gem.domain.Tool}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ToolResource {

    private final Logger log = LoggerFactory.getLogger(ToolResource.class);

    private static final String ENTITY_NAME = "tool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToolRepository toolRepository;

    public ToolResource(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    /**
     * {@code POST  /tools} : Create a new tool.
     *
     * @param tool the tool to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tool, or with status {@code 400 (Bad Request)} if the tool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tools")
    public ResponseEntity<Tool> createTool(@RequestBody Tool tool) throws URISyntaxException {
        log.debug("REST request to save Tool : {}", tool);
        if (tool.getId() != null) {
            throw new BadRequestAlertException("A new tool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tool result = toolRepository.save(tool);
        return ResponseEntity
            .created(new URI("/api/tools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tools/:id} : Updates an existing tool.
     *
     * @param id the id of the tool to save.
     * @param tool the tool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tool,
     * or with status {@code 400 (Bad Request)} if the tool is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tools/{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tool tool)
        throws URISyntaxException {
        log.debug("REST request to update Tool : {}, {}", id, tool);
        if (tool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tool result = toolRepository.save(tool);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tool.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tools/:id} : Partial updates given fields of an existing tool, field will ignore if it is null
     *
     * @param id the id of the tool to save.
     * @param tool the tool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tool,
     * or with status {@code 400 (Bad Request)} if the tool is not valid,
     * or with status {@code 404 (Not Found)} if the tool is not found,
     * or with status {@code 500 (Internal Server Error)} if the tool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tools/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tool> partialUpdateTool(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tool tool)
        throws URISyntaxException {
        log.debug("REST request to partial update Tool partially : {}, {}", id, tool);
        if (tool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!toolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tool> result = toolRepository
            .findById(tool.getId())
            .map(
                existingTool -> {
                    if (tool.getTitle() != null) {
                        existingTool.setTitle(tool.getTitle());
                    }
                    if (tool.getDescription() != null) {
                        existingTool.setDescription(tool.getDescription());
                    }
                    if (tool.getUrl() != null) {
                        existingTool.setUrl(tool.getUrl());
                    }
                    if (tool.getFile() != null) {
                        existingTool.setFile(tool.getFile());
                    }
                    if (tool.getFileContentType() != null) {
                        existingTool.setFileContentType(tool.getFileContentType());
                    }

                    return existingTool;
                }
            )
            .map(toolRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tool.getId().toString())
        );
    }

    /**
     * {@code GET  /tools} : get all the tools.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tools in body.
     */
    @GetMapping("/tools")
    public List<Tool> getAllTools() {
        log.debug("REST request to get all Tools");
        return toolRepository.findAll();
    }

    /**
     * {@code GET  /tools/:id} : get the "id" tool.
     *
     * @param id the id of the tool to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tool, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tools/{id}")
    public ResponseEntity<Tool> getTool(@PathVariable Long id) {
        log.debug("REST request to get Tool : {}", id);
        Optional<Tool> tool = toolRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tool);
    }

    /**
     * {@code DELETE  /tools/:id} : delete the "id" tool.
     *
     * @param id the id of the tool to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tools/{id}")
    public ResponseEntity<Void> deleteTool(@PathVariable Long id) {
        log.debug("REST request to delete Tool : {}", id);
        toolRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
