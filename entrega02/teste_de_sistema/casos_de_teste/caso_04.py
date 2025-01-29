# Fechar pedido
# não é possível executar pois depende do estoque, que não é posspivel atualizar

from helpers.usuario import MENU, login, hover_lateral_menu

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select

import time

driver = webdriver.Chrome()

def fechar_pedido(cliente, observacao, numero_pedido, forma_pagamento, valor_desconto):
    time.sleep(5)
    driver.find_element(By.XPATH, f"/html/body/section[2]/div/div/div[3]/div/div[{numero_pedido}]/div/div/a").click()

    time.sleep(5)
    dropdown = driver.find_element(By.ID, "cliente")
    drop = Select(dropdown).select_by_visible_text(cliente)

    driver.find_element(By.XPATH, """//*[@id="observacao"]""").send_keys(observacao)

    driver.find_element(By.XPATH, """//*[@id="btn-venda"]""").click()

    # driver.find_element(By.XPATH, """//*[@id="pagamento"]""").send_keys(forma_pagamento)
    time.sleep(5)
    dropdown = driver.find_element(By.ID, "pagamento")
    drop = Select(dropdown).select_by_visible_text(forma_pagamento)

    time.sleep(3)
    driver.find_element(By.XPATH, """//*[@id="desconto"]""").send_keys(valor_desconto)

    time.sleep(3)
    driver.find_element(By.XPATH, """//*[@id="form_pagamento"]/div[5]/a""").click()

    time.sleep(5)
    alerta = driver.switch_to.alert
    texto_alerta = alerta.text

    assert texto_alerta == "Pedido Fechado!", f"Texto do alerta é '{texto_alerta}'."

    alerta.accept()

try:
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    # abrir pedidos
    hover_lateral_menu(driver, "pedidos")

    fechar_pedido(
        cliente = "João Rafael Mendes Nogueira",
        observacao = "Observação Teste",
        numero_pedido = "1",
        forma_pagamento = "À Vista",
        valor_desconto = 0.1
    )

    time.sleep(5)
finally:
    # Fechar o navegador
    driver.quit()