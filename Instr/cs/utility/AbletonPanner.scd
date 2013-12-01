Instr("cs.utility.AbletonPanner", {

  arg bus, azimuth, distance, decoder;

  var out, inL, inR, components;

  inL = In.ar(bus, 1);
  inR = In.ar(bus, 2);

  /*components = BiPanB2.ar(inL, inR, azimuth);

  out = FoaDecode.ar(components, decoder);*/

  out = [inL, inR];

}, [

  \audiobus,
  \bipolar,
  \unipolar,
  ObjectSpec(0)

]);
