package steps;

import baseEntities.BaseStep;
import core.BrowsersService;
import elements.RadioButton;
import io.qameta.allure.Step;
import models.ProjectBuilder;
import models.ProjectBuilderError;
import org.openqa.selenium.By;
import org.testng.Assert;
import pages.*;

public class ProjectStep extends BaseStep {
    public ProjectStep(BrowsersService browsersService) {
        super(browsersService);
    }

    @Step("Создание нового проекта с данными из {projectBuilder}")
    public void addProject(ProjectBuilder projectBuilder) {
        ProjectPage projectPage = new ProjectPage(browsersService, false);
        projectPage.newProjectButton();
        NewProjectPage newProjectPage = new NewProjectPage(browsersService, false);
        newProjectPage.setName(projectBuilder.getProjectName());
        newProjectPage.cleanCode();
        newProjectPage.setCode(projectBuilder.getProjectCode());
        newProjectPage.setDescription(projectBuilder.getDescription());
        RadioButton radioButton = new RadioButton(browsersService, By.xpath("//input[@name ='access_type']"));
        radioButton.selectByIndex("public");
        newProjectPage.addButtonClick();

        Assert.assertEquals(new TestRepositoryPage(browsersService, false).titleText(), projectBuilder.getProjectName());

    }
    @Step("Удаление проекта c названием {projectName}")
    public void deleteProject(String projectName) {
        ProjectPage projectPage = new ProjectPage(browsersService, false);
        projectPage.getProjectButton(projectName).click();
        TestRepositoryPage testRepositoryPage = new TestRepositoryPage(browsersService, false);
        testRepositoryPage.settingsClick();
        SettingsPage settingsPage = new SettingsPage(browsersService, false);
        settingsPage.deleteButtonClick();
        DeleteProjectPage deleteProjectPage = new DeleteProjectPage(browsersService, false);
        deleteProjectPage.deleteButtonClick();

        Assert.assertTrue(new ProjectPage(browsersService, false).titleLabel.isDisplayed());

    }
    @Step("Попытка создания проекта с помощью {projectBuilderError} для проверки ввода граничных значений")
    public void addErrorCode(ProjectBuilderError projectBuilderError) {
        ProjectPage projectPage = new ProjectPage(browsersService, false);
        projectPage.newProjectButton();
        NewProjectPage newProjectPage = new NewProjectPage(browsersService, false);
        newProjectPage.setName(projectBuilderError.getProjectName());
        newProjectPage.cleanCode();
        newProjectPage.setCode(projectBuilderError.getIncorrectProjectCode());
        newProjectPage.setDescription(projectBuilderError.getDescription());
        newProjectPage.addButtonClick();

        Assert.assertEquals(newProjectPage.errorText(), "The code must be at least 2 characters.");
    }

    @Step("Загрузка в проект с названием {projectName} готового тест-кейcа")
    public void uploadingTestCase (String projectName, String pathToFile) {
        ProjectPage projectPage = new ProjectPage(browsersService,false);
        projectPage.projectButton(projectName);
        TestRepositoryPage testRepositoryPage = new TestRepositoryPage(browsersService, false);
        testRepositoryPage.importBtn();
        ImportTestCasesPage importTestCasesPage = new ImportTestCasesPage(browsersService, false);
        importTestCasesPage.setUploadFileButton(pathToFile);
        importTestCasesPage.importButton();
    }
}
