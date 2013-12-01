Instr("cs.utility.AbletonPanner", {

  arg bus, azimuth, distance, decoder;

  var out, inL, inR, components;

  inL = SoundIn.ar(bus);
  inR = SoundIn.ar(bus + 1);

  components = PanB2.ar(inL, azimuth);

  out = FoaDecode.ar(components, decoder);
  //out = DecodeB2.ar(4, components[0], components[1], components[2]);
}, [

  \audiobus,
  \bipolar,
  \unipolar,
  ObjectSpec(0)
]);
