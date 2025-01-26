from selenium.webdriver.common.by import By

MENU = "http://localhost:8080/login"

def login(driver):
    barra_usuario = driver.find_element(By.ID, "user")
    barra_usuario.send_keys("gerente")

    barra_senha = driver.find_element(By.ID, "password")
    barra_senha.send_keys("123")

    botao = driver.find_element(By.ID, "btn-login")
    botao.click()