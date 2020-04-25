package com.java.async.morden.dto;

import com.java.async.morden.constant.Discount;

import java.util.Random;

import static com.java.async.morden.util.Util.delay;
import static com.java.async.morden.util.Util.format;

public class Shop {

  private final String name;
  private final Random random;

  public Shop(String name) {
    this.name = name;
    random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
  }

  public String getPrice(String product) {
    double price = calculatePrice(product);
    Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
    return name + ":" + price + ":" + code;
  }

  public double calculatePrice(String product) {
    delay();
    return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
  }

  public String getName() {
    return name;
  }

}
