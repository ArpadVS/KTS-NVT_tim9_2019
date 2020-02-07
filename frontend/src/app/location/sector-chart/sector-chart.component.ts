import { Component, OnInit, Input } from '@angular/core';
import { Sector, SittingSector, StandingSector } from 'src/app/shared/models/hall.model';

@Component({
  selector: 'app-sector-chart',
  templateUrl: './sector-chart.component.html',
  styleUrls: ['./sector-chart.component.scss']
})
export class SectorChartComponent implements OnInit {

  @Input() sectors: Sector[];
  private seatConfig: any = [];
  private sectorConfig: any = [];
  private seatmap = [];
  private sectormap = [];
  private seatChartConfig = {
    showRowsLabel : true,
    showRowWisePricing : true,
    newSeatNoForRow : true
  };

  private cart = {
    selectedSeats : [],
    seatstoStore : [],
    totalamount : 0,
    cartId : '',
    eventId : 0
  };


  title = 'seat-chart-generator';
  constructor() { }

  ngOnInit() {
    // Process a simple bus layout
    /*this.seatConfig = [
      {
        seat_price: 250,
        seat_map: [
          {
            seat_label: '1',
            layout: '______'
          },
          {
            seat_label: '2',
            layout: 'gg__gg'
          },
          {
            seat_label: '3',
            layout: 'gg__gg'
          },
          {
            seat_label: '4',
            layout: 'gg__gg'
          },
          {
            seat_label: '5',
            layout: 'gg__gg'
          },
          {
            seat_label: '6',
            layout: 'gg__gg'
          },
          {
            seat_label: '7',
            layout: 'gg__gg'
          },
          {
            seat_label: '8',
            layout: 'gggggg'
          }
        ]
      }
    ];*/
    // this.processSeatChart(this.seatConfig);
    // this.blockSeats('7_1');
  }

  generateJson() {
    this.seatmap = [];
    console.log(this.sectors);
    this.sectors.forEach(sector => {
      this.seatConfig = [];
      const seatMap = [];
      if (sector.type === 'sitting') {
        const sit = sector as SittingSector;
        console.log((sector as SittingSector).numRows);
        for (let i = 0; i < sit.numRows; i++) {
          seatMap.push({
            seat_label: (i + 1).toString(),
            layout: 'g'.repeat(sit.numCols)
          });
        }
        seatMap.push({
          seat_label: '',
          layout: '_'.repeat(sit.numCols)
        });
      } else if (sector.type === 'standing') {
        console.log(sector.type);
        const stand = sector as StandingSector;
        for (let i = 0; i < 3; i++) {
          seatMap.push({
            seat_label: (i + 1).toString(),
            layout: '_'.repeat(3)
          });
        }
        seatMap.push({
          seat_label: '',
          layout: '_'.repeat(3)
        });
      }
      this.seatConfig.push({
        sector_id: sector.id,
        sector_name: sector.name,
        sector_type: sector.type,
        seat_price: 0,
        seat_map: seatMap
      });
      this.processSeatChart(this.seatConfig);
    });
  }
  processSeatChart( mapData: any[] ) {
      if ( mapData.length > 0 ) {
        let seatNoCounter = 1;
        // tslint:disable-next-line: prefer-for-of
        for (let counter = 0; counter < mapData.length; counter++) {
          let rowLabel = '';
          const itemMap = mapData[counter].seat_map;
          console.log(itemMap);
          // Get the label name and price
          // rowLabel = ''; // 'Row ' + itemMap[0].seat_label + ' - ';
          if ( itemMap[ itemMap.length - 1].seat_label !== ' ' ) {
            rowLabel += itemMap[ itemMap.length - 1].seat_label;
          } else {
            rowLabel += itemMap[ itemMap.length - 2].seat_label;
          }
          if (mapData[counter].sector_type === 'sitting') {
            rowLabel += 'Sitting ';
          } else if (mapData[counter].sector_type === 'standing') {
            rowLabel += 'Standing ';
          }
          rowLabel += 'sector: ' + mapData[counter].sector_name;
          // rowLabel += ' : Rs. ' + mapData[counter].seat_price;
          itemMap.forEach(mapElement => {
            console.log(mapElement.seat_label);
            const mapObj = {
              seatRowLabel: mapElement.seat_label,
              seats: [],
              seatPricingInformation: rowLabel
            };
            rowLabel = '';
            const seatValArr = mapElement.layout.split('');
            if ( this.seatChartConfig.newSeatNoForRow ) {
              seatNoCounter = 1; // Reset the seat label counter for new row
            }
            let totalItemCounter = 1;
            let label = '';
            if (mapData[counter].sector_type === 'standing') {
              label = 'stand';
            }
            seatValArr.forEach(item => {
              const seatObj = {
                key : mapElement.seat_label + '_' + totalItemCounter,
                price : mapData[counter].seat_price,
                status : 'available',
                seatLabel: label,
                seatNo: '',
                sectorId: mapData[counter].sector_id,
                sectorName: mapData[counter].sector_name
              };
              if ( item !== '_') {
                seatObj.seatLabel = mapElement.seat_label + ' ' + seatNoCounter;
                if (seatNoCounter < 10) {
                  seatObj.seatNo = '0' + seatNoCounter;
                } else {
                  seatObj.seatNo = '' + seatNoCounter;
                }
                seatNoCounter++;
              } else {
                seatObj.seatLabel = '';
              }
              totalItemCounter++;
              mapObj.seats.push(seatObj);
            });
            console.log(' \n\n\n Seat Objects ' , mapObj);
            this.seatmap.push( mapObj );
          });
        }
      }
}

public selectSeat( seatObject: any ) {
  console.log( 'Seat to block: ' , seatObject );
  if (seatObject.status === 'available') {
    seatObject.status = 'booked';
    this.cart.selectedSeats.push(seatObject.seatLabel);
    this.cart.seatstoStore.push(seatObject.key);
    this.cart.totalamount += seatObject.price;
  } else if ( seatObject.status === 'booked' ) {
    seatObject.status = 'available';
    const seatIndex = this.cart.selectedSeats.indexOf(seatObject.seatLabel);
    if ( seatIndex > -1) {
      this.cart.selectedSeats.splice(seatIndex , 1);
      this.cart.seatstoStore.splice(seatIndex , 1);
      this.cart.totalamount -= seatObject.price;
    }
  }
}

public blockSeats(seatsToBlock: string) {
  if (seatsToBlock !== '') {
    const seatsToBlockArr = seatsToBlock.split(',');
    // tslint:disable-next-line: prefer-for-of
    for (let index = 0; index < seatsToBlockArr.length; index++) {
      const seat =  seatsToBlockArr[index] + '';
      const seatSplitArr = seat.split('_');
      console.log('Split seat: ' , seatSplitArr);
      // tslint:disable-next-line: prefer-for-of
      for (let index2 = 0; index2 < this.seatmap.length; index2++) {
        const element = this.seatmap[index2];
        if (element.seatRowLabel === seatSplitArr[0]) {
          // tslint:disable-next-line: radix
          const seatObj = element.seats[parseInt(seatSplitArr[1]) - 1];
          if (seatObj) {
            console.log('\n\n\nFount Seat to block: ' , seatObj);
            seatObj.status = 'unavailable';
            // tslint:disable-next-line: radix
            this.seatmap[index2].seats[parseInt(seatSplitArr[1]) - 1] = seatObj;
            console.log('\n\n\nSeat Obj' , seatObj);
            // tslint:disable-next-line: radix
            console.log(this.seatmap[index2].seats[parseInt(seatSplitArr[1]) - 1]);
            break;
          }
        }
      }
    }
  }
}
}
