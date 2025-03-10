import { Injectable } from "@angular/core";
import { Promotion } from "../pages/promotions/promotion";
import { API_URL } from "../utils/contants";

@Injectable({
  providedIn: 'root'
})

export class PromotionService {

  public async getPromotionById(id: number): Promise<Promotion> {

    const response = await fetch(API_URL + '/promotions/' + id, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { promotion } = await response.json();

    return promotion ?? {};

  }

}
