package com.seventh7.mybatis.generate;

/**
 * @author yanglin
 */
public class PropertyGenerator {

    //  public static final PropertyNameStrategy PROPERTY_NAME_STRATEGY = new HumpStrategy();
    //
    //  public static void generateProperties(@NotNull Collection<DatabaseTableFieldData> columns,
    //                                        @NotNull GroupFour groupFour) {
    //    final XmlElement element = groupFour.getXmlElement();
    //    if (columns.isEmpty() || element == null)  {
    //      return;
    //    }
    //
    //    PropertyGroup property;
    //    for (DatabaseTableFieldData column : columns) {
    //      Set<DasColumn.Attribute> attrs = column.getTable().getColumnAttrs(column);
    //      boolean pk = attrs.contains(DasColumn.Attribute.PRIMARY_KEY);
    //      if (pk) {
    //        property = groupFour.addId();
    //      } else {
    //        property = groupFour.addResult();
    //      }
    //      setupProperties(column, property);
    //    }
    //    EditorService.getInstance(element.getProject()).format(element.getContainingFile(), groupFour.getXmlElement());
    //  }
    //
    //  private static void setupProperties(DatabaseTableFieldData column, PropertyGroup property) {
    //    property.getJdbcType().setStringValue(SQLConstants.getJdbcTypeName(column.getJdbcType()));
    //    final String columnName = column.getName();
    //    property.getProperty().setStringValue(PROPERTY_NAME_STRATEGY.apply(columnName));
    //    property.getColumn().setStringValue(columnName);
    //  }
    //
    //  interface PropertyNameStrategy {
    //    String apply(String columnName);
    //  }
    //
    //  /** The only strategy till now */
    //  public static class HumpStrategy implements PropertyNameStrategy {
    //
    //    @Override public String apply(String columnName) {
    //      StringBuilder sb = new StringBuilder();
    //      final String[] split = columnName.split("_");
    //      for (int i = 0; i < split.length; i++) {
    //        if (i == 0) {
    //          sb.append(WordUtils.uncapitalize(split[i]));
    //        } else {
    //          sb.append(WordUtils.capitalize(split[i]));
    //        }
    //      }
    //      return sb.toString();
    //    }
    //
    //  }

}
