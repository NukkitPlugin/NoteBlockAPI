package nukkitplugin.noteblockapi.util;

import java.io.*;
import java.util.HashMap;

import nukkitplugin.noteblockapi.element.Instrument;
import nukkitplugin.noteblockapi.element.Layer;
import nukkitplugin.noteblockapi.element.Note;
import nukkitplugin.noteblockapi.element.Song;

public class NBSDecoder {
	public static Song parse(File decodeFile) {
		try {
			return parse(new FileInputStream(decodeFile), decodeFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Song parse(InputStream inputStream) {
		return parse(inputStream, null); // Source is unknown -> no file
	}

	private static Song parse(InputStream inputStream, File decodeFile) {
		HashMap<Integer, Layer> layerHashMap = new HashMap<Integer, Layer>();
		try {
			DataInputStream dis = new DataInputStream(inputStream);
			short length = readShort(dis);
			short songHeight = readShort(dis);
			String title = readString(dis);
			String author = readString(dis);
			readString(dis);
			String description = readString(dis);
			float speed = readShort(dis) / 100f;
			dis.readBoolean(); // auto-save
			dis.readByte(); // auto-save duration
			dis.readByte(); // x/4ths, time signature
			readInt(dis); // minutes spent on project
			readInt(dis); // left clicks (why?)
			readInt(dis); // right clicks (why?)
			readInt(dis); // blocks added
			readInt(dis); // blocks removed
			readString(dis); // .mid/.schematic file name
			short tick = -1;
			while (true) {
				short jumpTicks = readShort(dis); // jumps till next tick
				if (jumpTicks == 0)
					break;
				tick += jumpTicks;
				short layer = -1;
				while (true) {
					short jumpLayers = readShort(dis); // jumps till next layer
					if (jumpLayers == 0)
						break;
					layer += jumpLayers;
					setNote(layer, tick, dis.readByte() /* instrument */, dis.readByte() /* note */, layerHashMap);
				}
			}
			for (int i = 0; i < songHeight; i++) {
				Layer layer = layerHashMap.get(i);
				if (layer != null) {
					layer.setName(readString(dis));
					layer.setVolume(dis.readByte());
				}
			}
			return new Song(speed, layerHashMap, songHeight, length, title, author, description, decodeFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void setNote(int newLayer, int ticks, byte instrument, byte key, HashMap<Integer, Layer> layerHashMap) {
		Layer layer = layerHashMap.get(newLayer);
		if (layer == null) {
			layer = new Layer();
			layerHashMap.put(newLayer, layer);
		}
		layer.setNote(ticks, new Note(Instrument.getInstrument(instrument), key));
	}

	private static short readShort(DataInputStream dis) throws IOException {
		int byte1 = dis.readUnsignedByte();
		int byte2 = dis.readUnsignedByte();
		return (short) (byte1 + (byte2 << 8));
	}

	private static int readInt(DataInputStream dis) throws IOException {
		int byte1 = dis.readUnsignedByte();
		int byte2 = dis.readUnsignedByte();
		int byte3 = dis.readUnsignedByte();
		int byte4 = dis.readUnsignedByte();
		return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
	}

	private static String readString(DataInputStream dis) throws IOException {
		int length = readInt(dis);
		StringBuilder sb = new StringBuilder(length);
		for (; length > 0; --length) {
			char c = (char) dis.readByte();
			if (c == (char) 0x0D)
				c = ' ';
			sb.append(c);
		}
		return sb.toString();
	}
}
