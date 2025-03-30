import { Injectable } from "@angular/core";
import { DonationPoint } from "../pages/donations_points/donationPoint";
import { API_URL } from "../utils/constants";

@Injectable({
  providedIn: 'root'
})

export class DonationPointService {

  public async getAllDonationPoints(): Promise<DonationPoint[]> {
    const response = await fetch(API_URL + '/donation-points/', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { donation_points } = await response.json();
    return donation_points ?? [];
  }

  public async getDonationPointById(id: number | null = null): Promise<DonationPoint> {
    const response = await fetch(API_URL + '/donation-points/' + id, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const { donation_point } = await response.json();
    return donation_point ?? {};
  }


  public async deleteDonationPoint(id: number): Promise<void> {
    const response = await fetch(API_URL + '/donation-points/' + id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      const body = await response.json();

      throw new Error('Error deleting donation point', {
        cause: body,
      });
    }
  }

}


