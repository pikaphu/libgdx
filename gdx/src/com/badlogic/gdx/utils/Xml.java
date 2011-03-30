// line 1 "Xml.rl"
// Do not edit this file! Generated by Ragel.

package com.badlogic.gdx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.files.FileHandle;

/**
 * Lightweight XML parser. Supports a subset of XML features: elements, attributes, text, predefined entities, CDATA, mixed
 * content. Namespaces are parsed as part of the element or attribute name. Prologs and doctypes are ignored. Only 8-bit character
 * encodings are supported. Input is assumed to be well formed.<br>
 * <br>
 * The default behavior is to parse the XML into a DOM. Extends this class and override methods to perform event driven parsing.
 * When this is done, the parse methods will return null.
 * @author Nathan Sweet
 */
public class Xml {
	private final ArrayList<Element> elements = new ArrayList(8);
	private Element root, current;
	private final StringBuilder textBuffer = new StringBuilder(64);

	public Element parse (FileHandle file) throws IOException {
		return parse(file.read());
	}

	public Element parse (String xml) {
		char[] data = xml.toCharArray();
		return parse(data, 0, data.length);
	}

	public Element parse (Reader reader) throws IOException {
		char[] data = new char[1024];
		int offset = 0;
		while (true) {
			int length = reader.read(data, offset, data.length - offset);
			if (length == -1) break;
			if (length == 0) {
				char[] newData = new char[data.length * 2];
				System.arraycopy(data, 0, newData, 0, data.length);
				data = newData;
			} else
				offset += length;
		}
		return parse(data, 0, offset);
	}

	public Element parse (InputStream input) throws IOException {
		return parse(new InputStreamReader(input, "ISO-8859-1"));
	}

	public Element parse (char[] data, int offset, int length) {
		int cs, p = offset, pe = length;

		int s = 0;
		String attributeName = null;
		boolean hasBody = false;

		// line 3 "../src/com/esotericsoftware/xml/Xml.java"
		{
			cs = xml_start;
		}

		// line 7 "../src/com/esotericsoftware/xml/Xml.java"
		{
			int _klen;
			int _trans = 0;
			int _acts;
			int _nacts;
			int _keys;
			int _goto_targ = 0;

			_goto:
			while (true) {
				switch (_goto_targ) {
				case 0:
					if (p == pe) {
						_goto_targ = 4;
						continue _goto;
					}
					if (cs == 0) {
						_goto_targ = 5;
						continue _goto;
					}
				case 1:
					_match:
					do {
						_keys = _xml_key_offsets[cs];
						_trans = _xml_index_offsets[cs];
						_klen = _xml_single_lengths[cs];
						if (_klen > 0) {
							int _lower = _keys;
							int _mid;
							int _upper = _keys + _klen - 1;
							while (true) {
								if (_upper < _lower) break;

								_mid = _lower + ((_upper - _lower) >> 1);
								if (data[p] < _xml_trans_keys[_mid])
									_upper = _mid - 1;
								else if (data[p] > _xml_trans_keys[_mid])
									_lower = _mid + 1;
								else {
									_trans += (_mid - _keys);
									break _match;
								}
							}
							_keys += _klen;
							_trans += _klen;
						}

						_klen = _xml_range_lengths[cs];
						if (_klen > 0) {
							int _lower = _keys;
							int _mid;
							int _upper = _keys + (_klen << 1) - 2;
							while (true) {
								if (_upper < _lower) break;

								_mid = _lower + (((_upper - _lower) >> 1) & ~1);
								if (data[p] < _xml_trans_keys[_mid])
									_upper = _mid - 2;
								else if (data[p] > _xml_trans_keys[_mid + 1])
									_lower = _mid + 2;
								else {
									_trans += ((_mid - _keys) >> 1);
									break _match;
								}
							}
							_trans += _klen;
						}
					} while (false);

					cs = _xml_trans_targs[_trans];

					if (_xml_trans_actions[_trans] != 0) {
						_acts = _xml_trans_actions[_trans];
						_nacts = (int)_xml_actions[_acts++];
						while (_nacts-- > 0) {
							switch (_xml_actions[_acts++]) {
							case 0:
								// line 63 "Xml.rl"
							{
								s = p;
							}
								break;
							case 1:
								// line 64 "Xml.rl"
							{
								char c = data[s];
								if (c == '?' || c == '!') {
									if (data[s + 1] == '[' && //
										data[s + 2] == 'C' && //
										data[s + 3] == 'D' && //
										data[s + 4] == 'A' && //
										data[s + 5] == 'T' && //
										data[s + 6] == 'A' && //
										data[s + 7] == '[') {
										s += 8;
										p = s + 2;
										while (data[p - 2] != ']' || data[p - 1] != ']' || data[p] != '>')
											p++;
										text(new String(data, s, p - s - 2));
									} else
										while (data[p] != '>')
											p++;
									{
										cs = 15;
										_goto_targ = 2;
										if (true) continue _goto;
									}
								}
								hasBody = true;
								open(new String(data, s, p - s));
							}
								break;
							case 2:
								// line 88 "Xml.rl"
							{
								hasBody = false;
								close();
							}
								break;
							case 3:
								// line 92 "Xml.rl"
							{
								close();
								{
									cs = 15;
									_goto_targ = 2;
									if (true) continue _goto;
								}
							}
								break;
							case 4:
								// line 96 "Xml.rl"
							{
								if (hasBody) {
									cs = 15;
									_goto_targ = 2;
									if (true) continue _goto;
								}
							}
								break;
							case 5:
								// line 99 "Xml.rl"
							{
								attributeName = new String(data, s, p - s);
							}
								break;
							case 6:
								// line 102 "Xml.rl"
							{
								attribute(attributeName, new String(data, s, p - s));
							}
								break;
							case 7:
								// line 105 "Xml.rl"
							{
								int end = p;
								while (end != s) {
									switch (data[end - 1]) {
									case ' ':
									case '\t':
									case '\n':
									case '\r':
										end--;
										continue;
									}
									break;
								}
								int current = s;
								boolean entityFound = false;
								while (current != end) {
									if (data[current++] != '&') continue;
									int entityStart = current;
									while (current != end) {
										if (data[current++] != ';') continue;
										textBuffer.append(data, s, entityStart - s - 1);
										String name = new String(data, entityStart, current - entityStart - 1);
										String value = entity(name);
										textBuffer.append(value != null ? value : name);
										s = current;
										entityFound = true;
										break;
									}
								}
								if (entityFound) {
									if (s < end) textBuffer.append(data, s, end - s);
									text(textBuffer.toString());
									textBuffer.setLength(0);
								} else
									text(new String(data, s, end - s));
							}
								break;
							// line 188 "../src/com/esotericsoftware/xml/Xml.java"
							}
						}
					}

				case 2:
					if (cs == 0) {
						_goto_targ = 5;
						continue _goto;
					}
					if (++p != pe) {
						_goto_targ = 1;
						continue _goto;
					}
				case 4:
				case 5:
				}
				break;
			}
		}

		// line 152 "Xml.rl"

		if (p < pe) {
			int lineNumber = 1;
			for (int i = 0; i < p; i++)
				if (data[i] == '\n') lineNumber++;
			throw new IllegalArgumentException("Error parsing XML on line " + lineNumber + " near: "
				+ new String(data, p, Math.min(32, pe - p)));
		} else if (!elements.isEmpty()) {
			Element element = elements.get(elements.size() - 1);
			elements.clear();
			throw new IllegalArgumentException("Error parsing XML, unclosed element: " + element.getName());
		}
		Element root = this.root;
		this.root = null;
		return root;
	}

	// line 208 "../src/com/esotericsoftware/xml/Xml.java"
	private static byte[] init__xml_actions_0 () {
		return new byte[] {0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 2, 0, 6, 2, 1, 4, 2, 2, 4};
	}

	private static final byte _xml_actions[] = init__xml_actions_0();

	private static byte[] init__xml_key_offsets_0 () {
		return new byte[] {0, 0, 4, 9, 14, 20, 26, 30, 35, 36, 37, 42, 46, 50, 51, 52, 56, 57, 62, 67, 73, 79, 83, 88, 89, 90, 95,
			99, 103, 107, 108, 109, 110, 111, 114};
	}

	private static final byte _xml_key_offsets[] = init__xml_key_offsets_0();

	private static char[] init__xml_trans_keys_0 () {
		return new char[] {32, 60, 9, 13, 32, 47, 62, 9, 13, 32, 47, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32,
			61, 9, 13, 32, 34, 39, 9, 13, 34, 34, 32, 47, 62, 9, 13, 32, 62, 9, 13, 32, 62, 9, 13, 39, 39, 32, 60, 9, 13, 60, 32,
			47, 62, 9, 13, 32, 47, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32, 47, 61, 62, 9, 13, 32, 61, 9, 13, 32, 34, 39, 9, 13, 34,
			34, 32, 47, 62, 9, 13, 32, 62, 9, 13, 32, 62, 9, 13, 32, 60, 9, 13, 39, 39, 62, 62, 32, 9, 13, 0};
	}

	private static final char _xml_trans_keys[] = init__xml_trans_keys_0();

	private static byte[] init__xml_single_lengths_0 () {
		return new byte[] {0, 2, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 1, 1, 2, 1, 3, 3, 4, 4, 2, 3, 1, 1, 3, 2, 2, 2, 1, 1, 1, 1, 1, 0};
	}

	private static final byte _xml_single_lengths[] = init__xml_single_lengths_0();

	private static byte[] init__xml_range_lengths_0 () {
		return new byte[] {0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0};
	}

	private static final byte _xml_range_lengths[] = init__xml_range_lengths_0();

	private static short[] init__xml_index_offsets_0 () {
		return new short[] {0, 0, 4, 9, 14, 20, 26, 30, 35, 37, 39, 44, 48, 52, 54, 56, 60, 62, 67, 72, 78, 84, 88, 93, 95, 97,
			102, 106, 110, 114, 116, 118, 120, 122, 125};
	}

	private static final short _xml_index_offsets[] = init__xml_index_offsets_0();

	private static byte[] init__xml_trans_targs_0 () {
		return new byte[] {1, 2, 1, 0, 2, 0, 0, 2, 3, 4, 11, 33, 4, 3, 4, 11, 0, 33, 4, 5, 6, 0, 7, 0, 6, 5, 6, 7, 6, 0, 7, 8, 13,
			7, 0, 10, 9, 10, 9, 4, 11, 33, 4, 0, 12, 33, 12, 0, 12, 33, 12, 0, 10, 14, 10, 14, 15, 17, 15, 16, 17, 16, 17, 31, 0,
			17, 18, 19, 26, 28, 19, 18, 19, 26, 0, 28, 19, 20, 21, 0, 22, 0, 21, 20, 21, 22, 21, 0, 22, 23, 29, 22, 0, 25, 24, 25,
			24, 19, 26, 28, 19, 0, 27, 28, 27, 0, 27, 28, 27, 0, 28, 17, 28, 0, 25, 30, 25, 30, 0, 32, 34, 32, 33, 33, 0, 0, 0};
	}

	private static final byte _xml_trans_targs[] = init__xml_trans_targs_0();

	private static byte[] init__xml_trans_actions_0 () {
		return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 3, 20, 3, 0, 0, 0, 0, 9, 0, 1, 11, 0, 11, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 17, 1, 13, 0, 0, 0, 9, 0, 0, 5, 23, 5, 0, 0, 9, 0, 0, 17, 1, 13, 0, 0, 0, 0, 1, 15, 0, 0, 0, 0, 0, 1, 3, 3, 20, 3,
			0, 0, 0, 0, 9, 0, 1, 11, 0, 11, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 1, 13, 0, 0, 0, 9, 0, 0, 5, 23, 5, 0, 0, 9, 0,
			0, 0, 0, 0, 0, 17, 1, 13, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0};
	}

	private static final byte _xml_trans_actions[] = init__xml_trans_actions_0();

	static final int xml_start = 1;
	static final int xml_first_final = 33;
	static final int xml_error = 0;

	static final int xml_en_elementBody = 15;
	static final int xml_en_main = 1;

	// line 170 "Xml.rl"

	protected void open (String name) {
		Element child = new Element(name);
		Element parent = current;
		if (parent != null) parent.addChild(child);
		elements.add(child);
		current = child;
	}

	protected void attribute (String name, String value) {
		current.setAttribute(name, value);
	}

	protected String entity (String name) {
		if (name.equals("lt")) return "<";
		if (name.equals("gt")) return ">";
		if (name.equals("amp")) return "&";
		if (name.equals("apos")) return "'";
		if (name.equals("quot")) return "\"";
		return null;
	}

	protected void text (String text) {
		String existing = current.getText();
		current.setText(existing != null ? existing + text : text);
	}

	protected void close () {
		int size = elements.size();
		root = elements.remove(size - 1);
		current = size > 1 ? elements.get(size - 2) : null;
	}

	static public class Element {
		private final String name;
		private HashMap<String, String> attributes;
		private ArrayList<Element> children;
		private String text;

		public Element (String name) {
			this.name = name;
		}

		public String getName () {
			return name;
		}

		public String getAttribute (String name) {
			if (attributes == null) return null;
			return attributes.get(name);
		}

		public void setAttribute (String name, String value) {
			if (attributes == null) attributes = new HashMap(8);
			attributes.put(name, value);
		}

		public int getChildCount () {
			if (children == null) return 0;
			return children.size();
		}

		public Element getChild (int i) {
			if (children == null) return null;
			return children.get(i);
		}

		public void addChild (Element element) {
			if (children == null) children = new ArrayList(8);
			children.add(element);
		}

		public String getText () {
			return text;
		}

		public void setText (String text) {
			this.text = text;
		}

		public String toString () {
			return toString("");
		}

		public String toString (String indent) {
			StringBuilder buffer = new StringBuilder(128);
			buffer.append(indent);
			buffer.append('<');
			buffer.append(name);
			if (attributes != null) {
				for (Entry<String, String> entry : attributes.entrySet()) {
					buffer.append(' ');
					buffer.append(entry.getKey());
					buffer.append("=\"");
					buffer.append(entry.getValue());
					buffer.append('\"');
				}
			}
			if (children == null && (text == null || text.length() == 0))
				buffer.append("/>");
			else {
				buffer.append(">\n");
				String childIndent = indent + '\t';
				if (text != null && text.length() > 0) {
					buffer.append(childIndent);
					buffer.append(text);
					buffer.append('\n');
				}
				if (children != null) {
					for (Element child : children) {
						buffer.append(child.toString(childIndent));
						buffer.append('\n');
					}
				}
				buffer.append(indent);
				buffer.append("</");
				buffer.append(name);
				buffer.append('>');
			}
			return buffer.toString();
		}
	}
}
