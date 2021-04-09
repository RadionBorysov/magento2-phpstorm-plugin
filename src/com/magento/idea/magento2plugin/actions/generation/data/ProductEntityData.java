/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.data;

import com.magento.idea.magento2plugin.magento.packages.File;
import com.magento.idea.magento2plugin.magento.packages.Package;
import com.magento.idea.magento2plugin.magento.packages.eav.EavEntities;

@SuppressWarnings({"PMD.TooManyFields"})
public class ProductEntityData implements EavEntityDataInterface {
    private String group;
    private String code;
    private String type;
    private String label;
    private String input;
    private String source;
    private String scope;
    private boolean required;
    private boolean usedInGrid;
    private boolean visibleInGrid;
    private boolean filterableInGrid;
    private boolean visible;
    private boolean htmlAllowedOnFront;
    private boolean visibleOnFront;
    private int sortOrder;

    private String dataPatchName;
    private String namespace;
    private String directory;
    private String moduleName;

    /**
     * Constructor.
     */
    public ProductEntityData() {
        this.required = false;
        this.usedInGrid = false;
        this.visibleInGrid = false;
        this.filterableInGrid = false;
        this.visible = true;
        this.htmlAllowedOnFront = false;
        this.visibleOnFront = false;
        this.sortOrder = 0;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setInput(final String input) {
        this.input = input;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public void setUsedInGrid(final boolean usedInGrid) {
        this.usedInGrid = usedInGrid;
    }

    public void setVisibleInGrid(final boolean visibleInGrid) {
        this.visibleInGrid = visibleInGrid;
    }

    public void setFilterableInGrid(final boolean filterableInGrid) {
        this.filterableInGrid = filterableInGrid;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public void setHtmlAllowedOnFront(final boolean htmlAllowedOnFront) {
        this.htmlAllowedOnFront = htmlAllowedOnFront;
    }

    public void setVisibleOnFront(final boolean visibleOnFront) {
        this.visibleOnFront = visibleOnFront;
    }

    public void setSortOrder(final int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setDataPatchName(final String dataPatchName) {
        this.dataPatchName = dataPatchName;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public void setDirectory(final String directory) {
        this.directory = directory;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getInput() {
        return input;
    }

    @Override
    public String getNamespace() {
        if (namespace == null) {
            namespace = getDataPathNamespace();
        }

        return namespace;
    }

    private String getDataPathNamespace() {
        final String[] parts = moduleName.split(Package.vendorModuleNameSeparator);
        if (parts[0] == null || parts[1] == null || parts.length > 2) {
            return null;
        }
        final String directoryPart = getDirectory().replace(
                File.separator,
                Package.fqnSeparator
        );
        return parts[0] + Package.fqnSeparator + parts[1] + Package.fqnSeparator + directoryPart;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getDirectory() {
        if (directory == null) {
            directory = "Setup/Patch/Data";
        }

        return directory;
    }

    @Override
    public String getDataPatchName() {
        return dataPatchName;
    }

    @Override
    public String getEntityClass() {
        return EavEntities.PRODUCT.getEntityClass();
    }

    public String getGroup() {
        return group;
    }

    public String getSource() {
        return source;
    }

    public String getScope() {
        return scope;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isUsedInGrid() {
        return usedInGrid;
    }

    public boolean isVisibleInGrid() {
        return visibleInGrid;
    }

    public boolean isFilterableInGrid() {
        return filterableInGrid;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isHtmlAllowedOnFront() {
        return htmlAllowedOnFront;
    }

    public boolean isVisibleOnFront() {
        return visibleOnFront;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}