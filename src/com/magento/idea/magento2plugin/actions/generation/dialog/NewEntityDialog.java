/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.dialog;

import com.google.common.base.CaseFormat;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.indexing.FileBasedIndex;
import com.magento.idea.magento2plugin.actions.generation.NewEntityAction;
import com.magento.idea.magento2plugin.actions.generation.OverrideClassByAPreferenceAction;
import com.magento.idea.magento2plugin.actions.generation.data.AclXmlData;
import com.magento.idea.magento2plugin.actions.generation.data.CollectionData;
import com.magento.idea.magento2plugin.actions.generation.data.ControllerFileData;
import com.magento.idea.magento2plugin.actions.generation.data.DataModelData;
import com.magento.idea.magento2plugin.actions.generation.data.DataModelInterfaceData;
import com.magento.idea.magento2plugin.actions.generation.data.DbSchemaXmlData;
import com.magento.idea.magento2plugin.actions.generation.data.LayoutXmlData;
import com.magento.idea.magento2plugin.actions.generation.data.MenuXmlData;
import com.magento.idea.magento2plugin.actions.generation.data.ModelData;
import com.magento.idea.magento2plugin.actions.generation.data.GetListQueryModelData;
import com.magento.idea.magento2plugin.actions.generation.data.PreferenceDiXmFileData;
import com.magento.idea.magento2plugin.actions.generation.data.ResourceModelData;
import com.magento.idea.magento2plugin.actions.generation.data.RoutesXmlData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentDataProviderData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentFormButtonData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentFormFieldData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentFormFieldsetData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentFormFileData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentGridData;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentGridToolbarData;
import com.magento.idea.magento2plugin.actions.generation.data.code.ClassPropertyData;
import com.magento.idea.magento2plugin.actions.generation.data.ui.ComboBoxItemData;
import com.magento.idea.magento2plugin.actions.generation.generator.AclXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.DataModelGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.DataModelInterfaceGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.DbSchemaWhitelistJsonGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.DbSchemaXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.LayoutXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.MenuXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.ModuleCollectionGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.ModuleControllerClassGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.ModuleModelGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.ModuleResourceModelGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.GetListQueryModelGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.PreferenceDiXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.RoutesXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.UiComponentDataProviderGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.UiComponentFormGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.UiComponentGridXmlGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.util.DbSchemaGeneratorUtil;
import com.magento.idea.magento2plugin.actions.generation.generator.util.NamespaceBuilder;
import com.magento.idea.magento2plugin.magento.files.ControllerBackendPhp;
import com.magento.idea.magento2plugin.magento.files.DataModel;
import com.magento.idea.magento2plugin.magento.files.DataModelInterface;
import com.magento.idea.magento2plugin.magento.files.ModelPhp;
import com.magento.idea.magento2plugin.magento.files.ModuleMenuXml;
import com.magento.idea.magento2plugin.magento.files.ResourceModelPhp;
import com.magento.idea.magento2plugin.magento.files.UiComponentDataProviderPhp;
import com.magento.idea.magento2plugin.magento.packages.Areas;
import com.magento.idea.magento2plugin.magento.packages.File;
import com.magento.idea.magento2plugin.magento.packages.HttpMethod;
import com.magento.idea.magento2plugin.magento.packages.PropertiesTypes;
import com.magento.idea.magento2plugin.magento.packages.database.TableEngines;
import com.magento.idea.magento2plugin.magento.packages.database.TableResources;
import com.magento.idea.magento2plugin.stubs.indexes.xml.MenuIndex;
import com.magento.idea.magento2plugin.ui.FilteredComboBox;
import com.magento.idea.magento2plugin.ui.table.TableGroupWrapper;
import com.magento.idea.magento2plugin.util.FirstLetterToLowercaseUtil;
import com.magento.idea.magento2plugin.util.GetPhpClassByFQN;
import com.magento.idea.magento2plugin.util.magento.GetAclResourcesListUtil;
import com.magento.idea.magento2plugin.util.magento.GetModuleNameByDirectoryUtil;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({
        "PMD.TooManyFields",
        "PMD.MissingSerialVersionUID",
        "PMD.DataClass",
        "PMD.UnusedPrivateField",
        "PMD.ExcessiveImports",
        "PMD.GodClass",
        "PMD.TooManyMethods",
        "PMD.CyclomaticComplexity"
})
public class NewEntityDialog extends AbstractDialog {
    @NotNull
    private final Project project;
    private final String moduleName;
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JPanel propertiesPanel;
    private JTable propertyTable;
    private JButton addProperty;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel generalTable;
    private JCheckBox createUiComponent;
    private JTextField entityName;
    private JLabel entityNameLabel;
    private JLabel dbTableNameLabel;
    private JTextField dbTableName;
    private JTextField entityId;
    private JLabel entityIdColumnNameLabel;
    private JTextField route;
    private JLabel routeLabel;
    private JTextField acl;
    private JLabel aclLabel;
    private JTextField aclTitle;
    private FilteredComboBox parentAcl;
    private JLabel formNameLabel;
    private JTextField formName;
    private JTextField formLabel;
    private JLabel formLabelLabel;
    private JTextField gridName;
    private JLabel gridNameLabel;
    private JLabel parentMenuItemLabel;
    private JLabel sortOrderLabel;
    private JLabel menuIdentifierLabel;
    private JTextField sortOrder;
    private JTextField menuIdentifier;
    private JLabel menuTitleLabel;
    private JTextField menuTitle;
    private FilteredComboBox parentMenu;
    private JLabel formMenuLabel;
    private JCheckBox addToolBar;
    private JCheckBox addBookmarksCheckBox;
    private JCheckBox addColumnsControlCheckBox;
    private JCheckBox addFullTextSearchCheckBox;
    private JCheckBox addListingFiltersCheckBox;
    private JCheckBox addListingPagingCheckBox;
    private JComboBox tableEngine;
    private JLabel tableEngineLabel;
    private JComboBox tableResource;
    private JLabel tableResourceLabel;
    private JCheckBox createInterface;
    private final List<String> properties;
    private TableGroupWrapper entityPropertiesTableGroupWrapper;

    private static final String ACTION_NAME = "Create Entity";
    private static final String PROPERTY_NAME = "Name";
    private static final String PROPERTY_TYPE = "Type";

    private JTextField observerName;

    /**
     * Constructor.
     *
     * @param project Project
     * @param directory PsiDirectory
     */
    public NewEntityDialog(final Project project, final PsiDirectory directory) {
        super();

        this.project = project;
        this.moduleName = GetModuleNameByDirectoryUtil.execute(directory, project);
        this.properties = new ArrayList<>();

        setContentPane(contentPane);
        setModal(true);
        setTitle(NewEntityAction.ACTION_DESCRIPTION);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener((final ActionEvent event) -> onOK());
        buttonCancel.addActionListener((final ActionEvent event) -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                onCancel();
            }
        });

        initializeComboboxSources();
        initPropertiesTable();

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                (final ActionEvent event) -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    /**
     * Open new controller dialog.
     *
     * @param project Project
     * @param directory PsiDirectory
     */
    public static void open(final Project project, final PsiDirectory directory) {
        final NewEntityDialog dialog = new NewEntityDialog(project, directory);
        dialog.pack();
        dialog.centerDialog(dialog);
        dialog.setVisible(true);
    }

    /**
     * Initialize combobox sources.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void initializeComboboxSources() {
        for (final String engine : TableEngines.getTableEnginesList()) {
            tableEngine.addItem(new ComboBoxItemData(engine, engine));
        }
        for (final String resource : TableResources.getTableResourcesList()) {
            tableResource.addItem(new ComboBoxItemData(resource, resource));
        }
    }

    /**
     * Initialize properties table.
     */
    private void initPropertiesTable() {
        final List<String> columns = new LinkedList<>(Arrays.asList(
                PROPERTY_NAME,
                PROPERTY_TYPE
        ));
        final Map<String, List<String>> sources = new HashMap<>();
        sources.put(PROPERTY_TYPE, PropertiesTypes.getPropertyTypesList());

        // Initialize entity properties Table Group
        entityPropertiesTableGroupWrapper = new TableGroupWrapper(
                propertyTable,
                addProperty,
                columns,
                new HashMap<>(),
                sources
        );
        entityPropertiesTableGroupWrapper.initTableGroup();
    }

    private DefaultTableModel getPropertiesTable() {
        return (DefaultTableModel) propertyTable.getModel();
    }

    /**
     * Perform code generation using input data.
     */
    private void onOK() {
        generateModelFile();
        generateResourceModelFile();
        generateCollectionFile();
        formatProperties();
        generateDataModelFile();

        if (createInterface.isSelected()) {
            generateDataModelInterfaceFile();
            generateDataModelPreference();
        }

        generateRoutesXmlFile();
        generateViewControllerFile();
        generateSubmitControllerFile();
        generateModelGetListQueryFile();
        generateDataProviderFile();
        generateLayoutFile();
        generateFormFile();
        generateAclXmlFile();
        generateGridViewControllerFile();
        generateGridLayoutFile();
        generateMenuFile();
        generateUiComponentGridFile();

        final DbSchemaXmlData dbSchemaXmlData = new DbSchemaXmlData(
                getDbTableName(),
                getTableResource(),
                getTableEngine(),
                getEntityName(),
                getEntityProperties()
        );

        generateDbSchemaXmlFile(dbSchemaXmlData);
        generateWhitelistJsonFile(dbSchemaXmlData);
        this.setVisible(false);
    }

    private PsiFile generateModelFile() {
        final NamespaceBuilder modelNamespace = getModelNamespace();
        final NamespaceBuilder resourceModelNamespace = getResourceModelNamespace();
        final String resourceModelName = getResourceModelName();


        return new ModuleModelGenerator(new ModelData(
            getModuleName(),
            getDbTableName(),
            getModelName(),
            resourceModelName,
            modelNamespace.getClassFqn(),
            modelNamespace.getNamespace(),
            resourceModelNamespace.getClassFqn()
        ), project).generate(ACTION_NAME, true);
    }

    private NamespaceBuilder getModelNamespace() {
        return new NamespaceBuilder(getModuleName(), getModelName(), ModelPhp.MODEL_DIRECTORY);
    }

    private NamespaceBuilder getDataModelNamespace() {
        return new NamespaceBuilder(getModuleName(), getDataModelName(), DataModel.DIRECTORY);
    }

    private NamespaceBuilder getDataModelInterfaceNamespace() {
        return new NamespaceBuilder(
                getModuleName(),
                getDataModelInterfaceName(),
                DataModelInterface.DIRECTORY
        );
    }

    private NamespaceBuilder getResourceModelNamespace() {
        return new NamespaceBuilder(
            getModuleName(),
            getResourceModelName(),
            ResourceModelPhp.RESOURCE_MODEL_DIRECTORY
        );
    }

    /**
     * Generate preference for data model.
     */
    private void generateDataModelPreference() {
        final NamespaceBuilder modelNamespace = getModelNamespace();
        final NamespaceBuilder modelInterfaceNamespace = getDataModelInterfaceNamespace();
        new PreferenceDiXmlGenerator(new PreferenceDiXmFileData(
                getModuleName(),
                GetPhpClassByFQN.getInstance(project).execute(
                        modelInterfaceNamespace.getClassFqn()
                ),
                modelNamespace.getClassFqn(),
                getModelName(),
                Areas.base.toString()
        ), project).generate(OverrideClassByAPreferenceAction.ACTION_NAME);
    }

    private String getModuleName() {
        return moduleName;
    }

    private String getEntityName() {
        return entityName.getText().trim();
    }

    private String getModelName() {
        return getEntityName().concat("Model");
    }

    private String getDataModelName() {
        return getEntityName().concat("Data");
    }

    /**
     * Get data provider class name.
     *
     * @return String
     */
    private String getDataProviderClassName() {
        return getEntityName().concat("DataProvider");
    }

    private String getDataModelInterfaceName() {
        return getEntityName().concat("Interface");
    }

    private String getResourceModelName() {
        return getEntityName().concat("Resource");
    }

    private String getCollectionName() {
        return getEntityName().concat("Collection");
    }

    private String getDbTableName() {
        return dbTableName.getText().trim();
    }

    private PsiFile generateResourceModelFile() {
        final NamespaceBuilder resourceModelNamespace = getResourceModelNamespace();
        return new ModuleResourceModelGenerator(new ResourceModelData(
            getModuleName(),
            getDbTableName(),
            getResourceModelName(),
            getEntityIdColumn(),
            resourceModelNamespace.getNamespace(),
            resourceModelNamespace.getClassFqn()
        ), project).generate(ACTION_NAME, true);
    }

    /**
     * Get entity id column name.
     *
     * @return String
     */
    private String getEntityIdColumn() {
        return entityId.getText().trim();
    }

    private PsiFile generateCollectionFile() {
        final NamespaceBuilder resourceModelNamespace = getResourceModelNamespace();
        final NamespaceBuilder modelNamespace = getModelNamespace();
        final NamespaceBuilder collectionNamespace = getCollectionNamespace();
        final StringBuilder modelFqn = new StringBuilder(modelNamespace.getClassFqn());
        final String modelName = getModelName();
        final StringBuilder resourceModelFqn
                = new StringBuilder(resourceModelNamespace.getClassFqn());
        final String resourceModelName = getResourceModelName();


        return new ModuleCollectionGenerator(new CollectionData(
            getModuleName(),
            getDbTableName(),
            modelName,
            getCollectionName(),
            collectionNamespace.getClassFqn(),
            getCollectionDirectory(),
            collectionNamespace.getNamespace(),
            resourceModelName,
            resourceModelFqn.toString(),
            modelFqn.toString()
        ), project).generate(ACTION_NAME, true);
    }

    /**
     * Generate Data Model File.
     */
    private void generateDataModelFile() {
        final NamespaceBuilder nameSpaceBuilder = getDataModelNamespace();
        new DataModelGenerator(project, new DataModelData(
            getDataModelNamespace().getNamespace(),
            getDataModelName(),
            getModuleName(),
            nameSpaceBuilder.getClassFqn(),
            getDataModelInterfaceNamespace().getClassFqn(),
            getProperties(),
            createInterface.isSelected()
        )).generate(ACTION_NAME, true);
    }

    private void generateDataModelInterfaceFile() {
        final NamespaceBuilder nameSpaceBuilder = getDataModelInterfaceNamespace();
        new DataModelInterfaceGenerator(project, new DataModelInterfaceData(
            nameSpaceBuilder.getNamespace(),
            getDataModelInterfaceName(),
            getModuleName(),
            nameSpaceBuilder.getClassFqn(),
            getProperties()
        )).generate(ACTION_NAME, true);
    }

    /**
     * Gets properties as a string, ready for templating.
     * "UPPER_SNAKE;lower_snake;type;UpperCamel;lowerCamel".
     */
    private String getProperties() {
        return StringUtils.join(properties, ",");
    }

    /**
     * Formats properties into an array of ClassPropertyData objects.
     */
    private void formatProperties() {
        final DefaultTableModel propertiesTable = getPropertiesTable();
        final int rowCount = propertiesTable.getRowCount();
        String name;
        String type;

        name = getEntityIdColumn();
        type = "int";
        properties.add(new ClassPropertyData(// NOPMD
                type,
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name),
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name),
                name,
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, name)
        ).string());

        for (int index = 0; index < rowCount; index++) {
            name = propertiesTable.getValueAt(index, 0).toString();
            type = propertiesTable.getValueAt(index, 1).toString();
            properties.add(new ClassPropertyData(// NOPMD
                    type,
                    CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name),
                    CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name),
                    name,
                    CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_UNDERSCORE, name)
            ).string());
        }
    }

    private NamespaceBuilder getCollectionNamespace() {
        return new NamespaceBuilder(
            getModuleName(),
            getCollectionName(),
            getCollectionDirectory()
        );
    }

    private String getCollectionDirectory() {
        return ResourceModelPhp.RESOURCE_MODEL_DIRECTORY + File.separator
            + getEntityName();
    }

    private PsiFile generateRoutesXmlFile() {
        return new RoutesXmlGenerator(new RoutesXmlData(
            Areas.adminhtml.toString(),
            getRoute(),
            getModuleName()
        ), project).generate(ACTION_NAME, false);
    }

    public String getRoute() {
        return route.getText().trim();
    }

    private PsiFile generateViewControllerFile() {
        final NamespaceBuilder namespace = new NamespaceBuilder(
                getModuleName(),
                getViewActionName(),
                getViewControllerDirectory()
        );
        return new ModuleControllerClassGenerator(new ControllerFileData(
                getViewControllerDirectory(),
                getViewActionName(),
                getModuleName(),
                Areas.adminhtml.toString(),
                HttpMethod.GET.toString(),
                getAcl(),
                true,
                namespace.getNamespace()
        ), project).generate(ACTION_NAME, false);
    }

    private String getViewActionName() {
        return "Edit";
    }

    private String getSubmitActionName() {
        return "Save";//NOPMD
    }

    private String getViewControllerDirectory() {
        return getControllerDirectory() + getModelName();
    }

    private String getControllerDirectory() {
        return ControllerBackendPhp.DEFAULT_DIR  + File.separator;
    }

    private PsiFile generateSubmitControllerFile() {
        final NamespaceBuilder namespace = new NamespaceBuilder(
                getModuleName(),
                getSubmitActionName(),
                getViewControllerDirectory()
        );
        return new ModuleControllerClassGenerator(new ControllerFileData(
                getViewControllerDirectory(),
                getSubmitActionName(),
                getModuleName(),
                Areas.adminhtml.toString(),
                HttpMethod.POST.toString(),
                getAcl(),
                true,
                namespace.getNamespace()
        ), project).generate(ACTION_NAME, false);
    }

    public String getAcl() {
        return acl.getText().trim();
    }

    /**
     * Generate data provider file.
     */
    private void generateDataProviderFile() {
        if (getDataProviderType().equals(UiComponentDataProviderPhp.CUSTOM_TYPE)) {
            final NamespaceBuilder namespaceBuilder = getDataProviderNamespace();
            new UiComponentDataProviderGenerator(new UiComponentDataProviderData(
                    getDataProviderClassName(),
                    namespaceBuilder.getNamespace(),
                    getDataProviderDirectory(),
                    getEntityIdColumn()
            ), getModuleName(), project).generate(ACTION_NAME, false);
        }
    }

    /**
     * Get data provider namespace builder.
     *
     * @return NamespaceBuilder
     */
    @NotNull
    private NamespaceBuilder getDataProviderNamespace() {
        return new NamespaceBuilder(
            getModuleName(),
            getDataProviderClassName(),
            getDataProviderDirectory()
        );
    }

    /**
     * Get data provider directory.
     *
     * @return String
     */
    public String getDataProviderDirectory() {
        // TODO: add ui part with dynamic implementation.
        return "UI/DataProvider";
    }

    /**
     * Get data provider type.
     *
     * @return String
     */
    public String getDataProviderType() {
        // TODO: add ui part with dynamic implementation.
        return UiComponentDataProviderPhp.CUSTOM_TYPE;
    }

    private PsiFile generateLayoutFile() {
        return new LayoutXmlGenerator(new LayoutXmlData(
            Areas.adminhtml.toString(),
            getRoute(),
            getModuleName(),
            getEntityName(),
            getViewActionName(),
            getFormName()
        ), project).generate(ACTION_NAME, false);
    }

    /**
     * Get controller name.
     *
     * @return String
     */
    public String getFormName() {
        return formName.getText().trim();
    }

    private PsiFile generateAclXmlFile() {
        return new AclXmlGenerator(new AclXmlData(
            getParentAcl(),
            getAcl(),
            getAclTitle()
        ), getModuleName(), project).generate(ACTION_NAME, false);
    }

    public String getParentAcl() {
        return parentAcl.getSelectedItem().toString().trim();
    }

    public String getAclTitle() {
        return aclTitle.getText().trim();
    }

    private PsiFile generateFormFile() {
        return new UiComponentFormGenerator(new UiComponentFormFileData(
            getFormName(),
            Areas.adminhtml.toString(),
            getModuleName(),
            getFormLabel(),
            getButtons(),
            getFieldsets(),
            getFields(),
            getRoute(),
            getEntityName(),
            getSubmitActionName(),
            getDataProviderNamespace().getClassFqn()
        ), project).generate(ACTION_NAME, true);
    }

    public String getFormLabel() {
        return formLabel.getText().trim();
    }

    /**
     * Returns form fieldsets.
     *
     * @return List[UiComponentFormFieldsetData]
     */
    public List<UiComponentFormFieldsetData> getFieldsets() {

        final ArrayList<UiComponentFormFieldsetData> fieldsets =
                new ArrayList<>();
        final UiComponentFormFieldsetData fieldsetData = new UiComponentFormFieldsetData(
                "general",
                "General",
                "10"
        );

        fieldsets.add(
                fieldsetData
        );

        return fieldsets;
    }

    /**
     * Return form buttons list.
     *
     * @return List[UiComponentFormButtonData]
     */
    protected List getButtons() {
        final List buttons = new ArrayList();
        final String directory = "Block/Form";

        final NamespaceBuilder namespaceBuilderSave = new NamespaceBuilder(
                getModuleName(),
                "Save",
                directory
        );
        buttons.add(new UiComponentFormButtonData(
                directory,
                "SaveEntity",
                getModuleName(),
                "Save",
                namespaceBuilderSave.getNamespace(),
                "Save Entity",
                "10",
                getFormName(),
                namespaceBuilderSave.getClassFqn()
        ));

        final NamespaceBuilder namespaceBuilderBack = new NamespaceBuilder(
                getModuleName(),
                "Back",
                directory
        );
        buttons.add(new UiComponentFormButtonData(
                directory,
                "Back",
                getModuleName(),
                "Back",
                namespaceBuilderBack.getNamespace(),
                "Back To Grid",
                "20",
                getFormName(),
                namespaceBuilderBack.getClassFqn()
        ));

        final NamespaceBuilder namespaceBuilderDelete = new NamespaceBuilder(
                getModuleName(),
                "Delete",
                directory
        );
        buttons.add(new UiComponentFormButtonData(
                directory,
                "Delete",
                getModuleName(),
                "Save",
                namespaceBuilderDelete.getNamespace(),
                "Delete Entity",
                "30",
                getFormName(),
                namespaceBuilderDelete.getClassFqn()
        ));
        return buttons;
    }

    /**
     * Returns form fields list.
     *
     * @return List[UiComponentFormFieldData]
     */
    public List<UiComponentFormFieldData> getFields() {
        final DefaultTableModel model = getPropertiesTable();
        final ArrayList<UiComponentFormFieldData> fieldsets = new ArrayList<>();

        fieldsets.add(
                new UiComponentFormFieldData(
                        "entity_id",
                        "int",
                        "Entity ID",
                        "0",
                        "general",
                        "hidden",
                        "entity_id"
                )
        );

        for (int count = 0; count < model.getRowCount(); count++) {

            final String name = model.getValueAt(count, 0).toString();
            final String dataType = model.getValueAt(count, 1).toString();

            final String label = model.getValueAt(count, 0).toString(); //todo: convert
            final String sortOrder = String.valueOf(count).concat("0");
            final String fieldset = "general";
            final String formElementType = model.getValueAt(count, 1).toString();
            final String source = model.getValueAt(count, 0).toString(); //todo: convert

            final UiComponentFormFieldData fieldsetData = new UiComponentFormFieldData(//NOPMD
                    name,
                    label,
                    sortOrder,
                    fieldset,
                    formElementType,
                    dataType,
                    source
            );

            fieldsets.add(
                    fieldsetData
            );
        }

        return fieldsets;
    }

    @SuppressWarnings({"PMD.UnusedPrivateMethod"})
    private void createUIComponents() {
        this.parentAcl = new FilteredComboBox(getAclResourcesList());
        this.parentMenu = new FilteredComboBox(getMenuReferences());

        if (getAclResourcesList().contains(ModuleMenuXml.defaultAcl)) {
            parentAcl.setSelectedItem(ModuleMenuXml.defaultAcl);
        }
    }

    private List<String> getAclResourcesList() {
        return GetAclResourcesListUtil.execute(project);
    }

    private PsiFile generateGridViewControllerFile() {
        final NamespaceBuilder namespace = new NamespaceBuilder(
                getModuleName(),
                "Listing",
                getControllerDirectory()
        );
        return new ModuleControllerClassGenerator(new ControllerFileData(
                getControllerDirectory(),
                "Listing",
                getModuleName(),
                Areas.adminhtml.toString(),
                HttpMethod.GET.toString(),
                getAcl(),
                true,
                namespace.getNamespace()
        ), project).generate(ACTION_NAME, false);
    }

    private PsiFile generateGridLayoutFile() {
        return new LayoutXmlGenerator(new LayoutXmlData(
                Areas.adminhtml.toString(),
                getRoute(),
                getModuleName(),
                getEntityName(),
                "Listing",
                getGridName()
        ), project).generate(ACTION_NAME, false);
    }

    private String getGridName() {
        return gridName.getText().toString();
    }

    private PsiFile generateMenuFile() {
        return new MenuXmlGenerator(new MenuXmlData(
            getParentMenuItem(),
            getSortOrder(),
            getModuleName(),
            getMenuIdentifier(),
            getMenuTitle(),
            getAcl(),
            getMenuAction()
        ), project).generate(ACTION_NAME, false);
    }

    private String getParentMenuItem() {
        return parentMenu.getSelectedItem().toString();
    }

    public String getSortOrder() {
        return sortOrder.getText().trim();
    }

    public String getMenuIdentifier() {
        return menuIdentifier.getText().trim();
    }

    private String getMenuAction() {
        return getRoute()
            + File.separator
            + FirstLetterToLowercaseUtil.convert(getEntityName())
            + File.separator
            + "listing";
    }

    public String getMenuTitle() {
        return menuTitle.getText().trim();
    }

    private void generateUiComponentGridFile() {
        final UiComponentGridXmlGenerator gridXmlGenerator = new UiComponentGridXmlGenerator(
                getUiComponentGridData(),
                project
        );
        gridXmlGenerator.generate(ACTION_NAME, true);
    }

    /**
     * Get grid UI component data.
     *
     * @return UiComponentGridData
     */
    public UiComponentGridData getUiComponentGridData() {
        return new UiComponentGridData(
            getModuleName(),
            Areas.adminhtml.toString(),
            getGridName(),
            getDataProviderNamespace().getClassFqn(),
            getEntityIdColumn(),
            getAcl(),
            getUiComponentGridToolbarData()
        );
    }

    /**
     * Get grid toolbar data.
     *
     * @return UiComponentGridToolbarData
     */
    public UiComponentGridToolbarData getUiComponentGridToolbarData() {
        return new UiComponentGridToolbarData(
            getAddToolBar(),
            getAddBookmarksCheckBox(),
            getAddColumnsControlCheckBox(),
            getAddFullTextSearchCheckBox(),
            getAddListingFiltersCheckBox(),
            getAddListingPagingCheckBox()
        );
    }

    private Boolean getAddToolBar() {
        return addToolBar.isSelected();
    }

    private Boolean getAddColumnsControlCheckBox() {
        return addColumnsControlCheckBox.isSelected();
    }

    private Boolean getAddFullTextSearchCheckBox() {
        return addFullTextSearchCheckBox.isSelected();
    }

    private Boolean getAddListingFiltersCheckBox() {
        return addListingFiltersCheckBox.isSelected();
    }

    private Boolean getAddListingPagingCheckBox() {
        return addListingPagingCheckBox.isSelected();
    }

    private Boolean getAddBookmarksCheckBox() {
        return addBookmarksCheckBox.isSelected();
    }

    /**
     * Run db_schema.xml file generator.
     *
     * @param dbSchemaXmlData DbSchemaXmlData
     */
    private void generateDbSchemaXmlFile(final @NotNull DbSchemaXmlData dbSchemaXmlData) {
        new DbSchemaXmlGenerator(
                dbSchemaXmlData,
                project,
                moduleName
        ).generate(ACTION_NAME, false);
    }

    /**
     * Run db_schema_whitelist.json generator.
     *
     * @param dbSchemaXmlData DbSchemaXmlData
     */
    private void generateWhitelistJsonFile(final @NotNull DbSchemaXmlData dbSchemaXmlData) {
        new DbSchemaWhitelistJsonGenerator(
                project,
                dbSchemaXmlData,
                moduleName
        ).generate(ACTION_NAME, false);
    }

    /**
     * Run GetListQuery.php file generator.
     */
    private void generateModelGetListQueryFile() {
        final String entityCollectionType = getCollectionNamespace().getClassFqn();

        new GetListQueryModelGenerator(
                new GetListQueryModelData(
                        getModuleName(),
                        getEntityName(),
                        entityCollectionType,
                        getEntityDataMapperType()
                ),
                project
        ).generate(ACTION_NAME, true);
    }

    /**
     * Get entity data mapper type.
     *
     * @return String
     */
    private String getEntityDataMapperType() {
        // TODO: implement with entity data mapper generation.
        return "Test\\Test\\Mapper\\" + getEntityName() + "DataMapper";
    }

    /**
     * Get tableResource field value.
     *
     * @return String
     */
    private String getTableResource() {
        return tableResource.getSelectedItem().toString().trim();
    }

    /**
     * Get tableEngine field value.
     *
     * @return String
     */
    private String getTableEngine() {
        return tableEngine.getSelectedItem().toString().trim();
    }

    /**
     * Get entity properties table columns data and format to suitable for generator.
     *
     * @return List of entity properties stored in HashMap.
     */
    private List<Map<String, String>> getEntityProperties() {
        final List<Map<String, String>> shortColumnsData =
                entityPropertiesTableGroupWrapper.getColumnsData();

        final List<Map<String, String>> columnsData =
                DbSchemaGeneratorUtil.complementShortPropertiesByDefaults(shortColumnsData);
        columnsData.add(0, DbSchemaGeneratorUtil.getTableIdentityColumnData(getEntityIdColumn()));

        return columnsData;
    }

    @NotNull
    private List<String> getMenuReferences() {
        final Collection<String> menuReferences
                = FileBasedIndex.getInstance().getAllKeys(MenuIndex.KEY, project);
        final ArrayList<String> menuReferencesList = new ArrayList<>(menuReferences);
        Collections.sort(menuReferencesList);

        return menuReferencesList;
    }
}