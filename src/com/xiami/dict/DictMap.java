package com.xiami.dict;

import java.util.ArrayList;
import java.util.HashMap;

import com.xiami.bean.DictWordBean;

public class DictMap {

	private static HashMap<String, ArrayList<DictWordBean>> eIndexMap = new HashMap<String, ArrayList<DictWordBean>>();
	private static HashMap<String, ArrayList<DictWordBean>> jIndexMap = new HashMap<String, ArrayList<DictWordBean>>();
	private static HashMap<String, String> eInputMap = new HashMap<String, String>();
	private static HashMap<String, String> jInputMap = new HashMap<String, String>();
	
	static{
		
		eIndexMap.put("a", new ArrayList<DictWordBean>());
		eIndexMap.put("b", new ArrayList<DictWordBean>());
		eIndexMap.put("c", new ArrayList<DictWordBean>());
		eIndexMap.put("d", new ArrayList<DictWordBean>());
		eIndexMap.put("e", new ArrayList<DictWordBean>());
		eIndexMap.put("f", new ArrayList<DictWordBean>());
		eIndexMap.put("g", new ArrayList<DictWordBean>());
		eIndexMap.put("h", new ArrayList<DictWordBean>());
		eIndexMap.put("i", new ArrayList<DictWordBean>());
		eIndexMap.put("j", new ArrayList<DictWordBean>());
		eIndexMap.put("k", new ArrayList<DictWordBean>());
		eIndexMap.put("l", new ArrayList<DictWordBean>());
		eIndexMap.put("m", new ArrayList<DictWordBean>());
		eIndexMap.put("n", new ArrayList<DictWordBean>());
		eIndexMap.put("o", new ArrayList<DictWordBean>());
		eIndexMap.put("p", new ArrayList<DictWordBean>());
		eIndexMap.put("q", new ArrayList<DictWordBean>());
		eIndexMap.put("r", new ArrayList<DictWordBean>());
		eIndexMap.put("s", new ArrayList<DictWordBean>());
		eIndexMap.put("t", new ArrayList<DictWordBean>());
		eIndexMap.put("u", new ArrayList<DictWordBean>());
		eIndexMap.put("v", new ArrayList<DictWordBean>());
		eIndexMap.put("w", new ArrayList<DictWordBean>());
		eIndexMap.put("x", new ArrayList<DictWordBean>());
		eIndexMap.put("y", new ArrayList<DictWordBean>());
		eIndexMap.put("z", new ArrayList<DictWordBean>());
		
		jIndexMap.put("a", new ArrayList<DictWordBean>());
		jIndexMap.put("k", new ArrayList<DictWordBean>());
		jIndexMap.put("s", new ArrayList<DictWordBean>());
		jIndexMap.put("t", new ArrayList<DictWordBean>());
		jIndexMap.put("n", new ArrayList<DictWordBean>());
		jIndexMap.put("h", new ArrayList<DictWordBean>());
		jIndexMap.put("m", new ArrayList<DictWordBean>());
		jIndexMap.put("y", new ArrayList<DictWordBean>());
		jIndexMap.put("r", new ArrayList<DictWordBean>());


		eInputMap.put("a", "dict/english/e_a.dict");
		eInputMap.put("b", "dict/english/e_b.dict");
		eInputMap.put("c", "dict/english/e_c.dict");
		eInputMap.put("d", "dict/english/e_d.dict");
		eInputMap.put("e", "dict/english/e_e.dict");
		eInputMap.put("f", "dict/english/e_f.dict");
		eInputMap.put("g", "dict/english/e_g.dict");
		eInputMap.put("h", "dict/english/e_h.dict");
		eInputMap.put("i", "dict/english/e_i.dict");
		eInputMap.put("j", "dict/english/e_j.dict");
		eInputMap.put("k", "dict/english/e_k.dict");
		eInputMap.put("l", "dict/english/e_l.dict");
		eInputMap.put("m", "dict/english/e_m.dict");
		eInputMap.put("n", "dict/english/e_n.dict");
		eInputMap.put("o", "dict/english/e_o.dict");
		eInputMap.put("p", "dict/english/e_p.dict");
		eInputMap.put("q", "dict/english/e_q.dict");
		eInputMap.put("r", "dict/english/e_r.dict");
		eInputMap.put("s", "dict/english/e_s.dict");
		eInputMap.put("t", "dict/english/e_t.dict");
		eInputMap.put("u", "dict/english/e_u.dict");
		eInputMap.put("v", "dict/english/e_v.dict");
		eInputMap.put("w", "dict/english/e_w.dict");
		eInputMap.put("x", "dict/english/e_x.dict");
		eInputMap.put("y", "dict/english/e_y.dict");
		eInputMap.put("z", "dict/english/e_z.dict");
		
		jInputMap.put("a", "dict/japanese/jp_a.dict");
		jInputMap.put("k", "dict/japanese/jp_k.dict");
		jInputMap.put("s", "dict/japanese/jp_s.dict");
		jInputMap.put("t", "dict/japanese/jp_t.dict");
		jInputMap.put("n", "dict/japanese/jp_n.dict");
		jInputMap.put("h", "dict/japanese/jp_h.dict");
		jInputMap.put("m", "dict/japanese/jp_m.dict");
		jInputMap.put("y", "dict/japanese/jp_y.dict");
		jInputMap.put("r", "dict/japanese/jp_r.dict");
	}

//	public static void initialWordList(DictWordBean bean) {
//		String s = bean.getWord().substring(0, 1).toLowerCase();
//		switch (s.hashCode()) {
//		case 97:
//			eIndexMap.get("a").add(bean);
//			break;
//		case 98:
//			eIndexMap.get("b").add(bean);
//			break;
//		case 99:
//			eIndexMap.get("c").add(bean);
//			break;
//		case 100:
//			eIndexMap.get("d").add(bean);
//			break;
//		case 101:
//			eIndexMap.get("e").add(bean);
//			break;
//		case 102:
//			eIndexMap.get("f").add(bean);
//			break;
//		case 103:
//			eIndexMap.get("g").add(bean);
//			break;
//		case 104:
//			eIndexMap.get("h").add(bean);
//			break;
//		case 105:
//			eIndexMap.get("i").add(bean);
//			break;
//		case 106:
//			eIndexMap.get("j").add(bean);
//			break;
//		case 107:
//			eIndexMap.get("k").add(bean);
//			break;
//		case 108:
//			eIndexMap.get("l").add(bean);
//			break;
//		case 109:
//			eIndexMap.get("m").add(bean);
//			break;
//		case 110:
//			eIndexMap.get("n").add(bean);
//			break;
//		case 111:
//			eIndexMap.get("o").add(bean);
//			break;
//		case 112:
//			eIndexMap.get("p").add(bean);
//			break;
//		case 113:
//			eIndexMap.get("q").add(bean);
//			break;
//		case 114:
//			eIndexMap.get("r").add(bean);
//			break;
//		case 115:
//			eIndexMap.get("s").add(bean);
//			break;
//		case 116:
//			eIndexMap.get("t").add(bean);
//			break;
//		case 117:
//			eIndexMap.get("u").add(bean);
//			break;
//		case 118:
//			eIndexMap.get("v").add(bean);
//			break;
//		case 119:
//			eIndexMap.get("w").add(bean);
//			break;
//		case 120:
//			eIndexMap.get("x").add(bean);
//			break;
//		case 121:
//			eIndexMap.get("y").add(bean);
//			break;
//		case 122:
//			eIndexMap.get("z").add(bean);
//			break;
//		default:
//			return;
//		}
//	}

//	public static ArrayList<DictWordBean> getKeyWordList(String prefix) {
//		String s = prefix.toLowerCase();
//		switch (s.hashCode()) {
//		case 97:
//			return eIndexMap.get("a");
//		case 98:
//			return eIndexMap.get("b");
//		case 99:
//			return eIndexMap.get("c");
//		case 100:
//			return eIndexMap.get("d");
//		case 101:
//			return eIndexMap.get("e");
//		case 102:
//			return eIndexMap.get("f");
//		case 103:
//			return eIndexMap.get("g");
//		case 104:
//			return eIndexMap.get("h");
//		case 105:
//			return eIndexMap.get("i");
//		case 106:
//			return eIndexMap.get("j");
//		case 107:
//			return eIndexMap.get("k");
//		case 108:
//			return eIndexMap.get("l");
//		case 109:
//			return eIndexMap.get("m");
//		case 110:
//			return eIndexMap.get("n");
//		case 111:
//			return eIndexMap.get("o");
//		case 112:
//			return eIndexMap.get("p");
//		case 113:
//			return eIndexMap.get("q");
//		case 114:
//			return eIndexMap.get("r");
//		case 115:
//			return eIndexMap.get("s");
//		case 116:
//			return eIndexMap.get("t");
//		case 117:
//			return eIndexMap.get("u");
//		case 118:
//			return eIndexMap.get("v");
//		case 119:
//			return eIndexMap.get("w");
//		case 120:
//			return eIndexMap.get("x");
//		case 121:
//			return eIndexMap.get("y");
//		case 122:
//			return eIndexMap.get("z");
//		default:
//			return eIndexMap.get("a");
//		}
//	}

	public static HashMap<String, ArrayList<DictWordBean>> getEIndexMap() {
		return eIndexMap;
	}

	public static void setEIndexMap(HashMap<String, ArrayList<DictWordBean>> map) {
		eIndexMap = map;
	}
	
	public static HashMap<String, String> getEInputMap() {
		return eInputMap;
	}

	public static void setEInputMap(HashMap<String, String> inputMap) {
		eInputMap = inputMap;
	}

	public static HashMap<String, ArrayList<DictWordBean>> getJIndexMap() {
		return jIndexMap;
	}

	public static void setJIndexMap(
			HashMap<String, ArrayList<DictWordBean>> indexMap) {
		jIndexMap = indexMap;
	}

	public static HashMap<String, String> getJInputMap() {
		return jInputMap;
	}

	public static void setJInputMap(HashMap<String, String> inputMap) {
		jInputMap = inputMap;
	}



}
