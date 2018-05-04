const googleMapsApiUrl = 'https://www.google.com/maps/search/?api=1';

export function buildMapQueryByAddress(address:string):string{
  // return '&query=47.5951518,-122.3316393';
  return googleMapsApiUrl+`&query=${encodeURIComponent(address)}`;
}

export function buildMapQueryByCoordinates(lat:number, lon:number):string{
  // return '&query=47.5951518,-122.3316393';
  return googleMapsApiUrl+`&query=${lat},${lon}`;
}
