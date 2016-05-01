'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('Feedify', function() {


  it('should automatically redirect to /signin when the user is not logged', function() {
    browser.get('/#/app/home');
    expect(browser.getLocationAbsUrl()).toMatch("/signin");
  });

it('should stay on login page on false identification', function() {
    browser.get('/#/app/home');
    element(by.model('user.username')).sendKeys('fakeusername');
    element(by.model('user.password')).sendKeys('fakepassword');
    element(by.buttonText('Sign in')).click();
    expect(browser.getLocationAbsUrl()).toMatch("/signin");
  });

  it('should redirect to home on valid authentication', function() {
    browser.get('/#/app/home');
    element(by.model('user.username')).sendKeys('alexisld');
    element(by.model('user.password')).sendKeys('toto');
    element(by.buttonText('Sign in')).click();
    expect(browser.getLocationAbsUrl()).toMatch("/app/home");
  });

  it('should display at least one user on setup', function() {
    browser.get('/#/app/setup');
    var eleme = $('td');
    expect(eleme.isDisplayed()).toBe(true);
  });

  it('should display at least one article on home view', function() {
    browser.get('/#/app/home');
    var eleme = $('.article-panel');
    expect(eleme.isDisplayed()).toBe(true);
  });


/*
  describe('DDN users page', function() {
    beforeEach(function() {
      browser.get('/#/users');
    });

    it('should be displaying the page correctly', function() {
      var elems = [];
      elems.push(element(by.buttonText('Create new group')));
      elems.push(element(by.buttonText('Create new user')));


      for (var i = 0; i < elems.length; i++) {
        expect(elems[i].isPresent()).toBe(true);
      }
    });

    it('buttons for adding users and rights should be working correctly', function() {

      expect(element.all(by.model('userToAdd')).get(0).isDisplayed()).toBe(false);
      element.all(by.css('[ng-click="addUserForm = !addUserForm"]')).get(0).click();
      expect(element.all(by.model('userToAdd')).get(0).isDisplayed()).toBe(true);
      element.all(by.css('[ng-click="addUserForm = !addUserForm"]')).get(0).click();
      expect(element.all(by.model('userToAdd')).get(0).isDisplayed()).toBe(false);

    });


  });

*/

/*  describe('view1', function() {

    beforeEach(function() {
      browser.get('index.html#/view1');
    });


    it('should render view1 when user navigates to /view1', function() {
      expect(element.all(by.css('[ng-view] p')).first().getText()).
        toMatch(/partial for view 1/);
    });

  });*/



});
