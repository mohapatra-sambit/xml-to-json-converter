# XML to JSON Converter

## Overview

The XML to JSON Converter is a flexible java-based tool/library designed to convert any XML input into JSON format. This offers a generic tool for converting XML files into JSON format, providing flexibility for various use cases. It can be used as a standalone command-line application or as a library integrated into other Java applications. That means, that this tool offers flexibility and robustness for integration into various applications. Users need to provide a JSON schema, which guides the conversion process from XML to JSON. If the JSON schema is not provided, default XML to JSON conversion would take place. However, if provided, the output is expected to adhere strictly to the provided JSON schema. Details about the JSON schema are given [below](#schema).

## Features

- **Generic Conversion**: Supports converting any XML structure to any JSON based on a provided schema.
- **Command-Line Interface (CLI)**: Can be used directly from the command line for quick conversions.
- **Library Integration**: Easily integrate the converter into Java projects as a library dependency.
- **Schema-Based Parsing**: The application parses the provided JSON schema to map XML elements and attributes into the corresponding JSON structure.
- **Default Conversion Logic**: In the absence of any user-defined schema, the default of conversion is applied.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven (for building the project)

### <a name="build"></a>Building the Project

To build the project and create a JAR file, clone the repository and run the following command:

```bash
git clone https://github.com/mohapatra-sambit/xml-to-json-converter.git
cd xml-to-json-converter
mvn clean package
```

This will generate couple of JAR files in the `target` directory:

1. `XML2JSON-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

2. `XML2JSON-0.0.1-SNAPSHOT.jar`

### <a name="depend"></a>Dependencies

The following are the third-party dependencies:

1. json ([json-20240303.jar](https://github.com/stleary/JSON-java))

2. org-apache-commons-lang3 ([org-apache-commons-lang3-RELEASE130.jar](https://commons.apache.org/proper/commons-lang/))

3. assertj-core ([assertj-core-3.26.3.jar](https://github.com/assertj/assertj)): For Unit Testing.

4. junit ([junit-4.13.2.jar](https://junit.org/junit4/)): For Unit Testing.

### Using the Converter as a CLI Application

Once you have the JAR file, you can use the converter from the command line. Here’s the basic syntax:

```bash
java -DInputXml=<PATH_OF_INPUT_XML_FILE> -DSchemaJson=<PATH_OF_SCHEMA_JSON_FILE> -DOutputJson=<PATH_OF_OUTPUT_JSON_FILE> -jar XML2JSON-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

- `--InputXml`: Path to the XML file to be converted (**Mandatory**).
- `--SchemaJson`: Path to the JSON schema file.
- `--OutputJson`: Path to the output JSON file.

#### Example

```bash
java -DInputXml=/home/xyz/input.xml -DSchemaJson=/home/xyz/schema.json -DOutputJson=/home/xyz/out.json -jar XML2JSON-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

#### Important Points

1. If the `SchemaJson` is not provided in the argument, the default conversion logic is applied to the input XML.

2. If the `OutputJson` is not provided in the argument, the converted JSON result is printed into the console/terminal.

### Using the Converter as a Library

To use the XML to JSON Converter as a library in your Java project, add any of the [above-mentioned](#build) JAR file to your project’s classpath.

**Note**: If you have added the `XML2JSON-0.0.1-SNAPSHOT.jar` in your project's classpath, then you must add the other [dependencies](#depend) manually into your project.

## <a name="schema"></a>Providing the JSON Schema

### Fundamentals

1. This XML to JSON converter looks out for a JSON schema definition to be applied to the input XML and form the converted output JSON.

2. There are multiple ways this JSON schema definition can be provided:
   
   - **Implicit Defining**: The input XML itself contains the JSON schema definition.
     
     - Introduce a new child element to the root element of the input XML with the name _JSONSchema_.
     
     - Add the entire JSON schema string as a value to this new element.
     
     - Pass this updated XML to the API. (Refer [Javadocs](https://mohapatra-sambit.github.io/xml-to-json-converter/) here)
     
     - **Example**,
       
       ```xml
       <root>
           <elementOne>valueOne</elementOne>
           <elementTwo attributeOne="attributeValueOne">valueTwo</elementTwo>
           ....
           <JSONSchema>
           {
               "FieldOne": "",
               "FieldTwo": ""
               "FieldThree": []
           }
           </JSONSchema>
       <root>
       ```
   
   - **Explicit Defining**: The input XML and the JSON schema are defined separately and passed onto the API for the conversion process.
     
     - The input XML can be passed as a [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) or [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) or [Document](https://docs.oracle.com/javase/8/docs/api/org/w3c/dom/Document.html) object.
     
     - Similarly, the schema JSON can be passed as [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) or [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) or [JSONObject](https://javadoc.io/doc/org.json/json/latest/index.html) object.
     
     - Refer [Javadocs](https://mohapatra-sambit.github.io/xml-to-json-converter/) here for the API specifications.

3- **Structure Mapping**: The JSON schema definition maps the required XML element and attribute to the *key-value* pairs in JSON format.

4- **Data Types**: The expected data types for each JSON field can be specified as well.

- Currently, the supported data types are Integer, Double, and Boolean. Apart from these, everything else is considered to be String.

5- **Recurring Fields**: Using recurring fields helps create complex JSON structures fields such as arrays in the output (if required).

### <a name="json-example"></a>Example JSON Schema

Here is an example of a simple JSON schema (<u>of course, without the dotted lines and the numbers at the end of each line</u>):

```json5
{
    "ShipmentIdentifier": "XPATH(/MyShipments/Shipment/@Shipment_Key)",............(1)
    "Type": "XPATH(/MyShipments/Shipment/Lines/Line/Order/@Type)",.................(2)
    "OrderNo": "VALUE(/MyShipments/Shipment/Lines/Line/Order/@Order_Num)",.........(3)
    "Carrier": "XYZ-ABCD Logistics",...............................................(4)
    "LineItems" : [{...............................................................(5)
        "recurrent_path": "/MyShipments/Shipment/Lines/Line",......................(6)
        "Identifier": "RECUR_ELEM(/Item/@Id)",.....................................(7)
        "Quantity": "RECUR_ELEM(/Item/@Qty):INT",..................................(8)
        "Description": "RECUR_ELEM(/Item/@Desc)",..................................(9)
        "Status": "RECUR_ELEM(/Order/@Status)",....................................(10)
        "Price": "RECUR_ELEM(/Item/@Cost):DBL",....................................(11)
        "Field1": "CONSTANT1",.....................................................(12)
        "Field2": 1000,............................................................(13)
        "Field3": "XPATH(/MyShipments/Shipment/@Number)",..........................(14)
        "Field4": "VALUE(/MyShipments/Shipment/@Number):INT",......................(15)
        "Field4": "XPATH(/MyShipments/Shipment/Organization)"......................(16)
    }],
    "Temp": [......................................................................(17)
        "VALUE(/MyShipments/Shipment/Organization)",...............................(18)
        1234,......................................................................(19)
        "ABCD",....................................................................(20)
        "XPATH(/MyShipments/Shipment/Lines/Line/Order/@Order_Num)".................(21)
    ],
    "OrderTempInfo": "XPATH(/MyShipments/Shipment/Lines/Line/Order)"...............(22)
}
```

The corresponding sample input XML is:<a name="xml-example"></a>

```xml
<MyShipments>
    <Shipment Shipment_Key="1202103301135561781564861" Number="528630">
        <Lines>
            <Line Line_Key="1202103301135561781564831">
                <Order Status="113350" Status_Desc="Included In Shipment" Order_Key="20210330113201178156395" Order_Num="22268086" Type="COD" />
                <Item Id="I1" Desc="Shoes" Qty="1" Cost="150.99"/>
            </Line>
            <Line Line_Key="1202103301135561781564841">
                <Order Status="113351" Status_Desc="Included In Shipment" Order_Key="20210330113201178156395" Order_Num="22268086" Type="RPR" />
                <Item Id="I2" Desc="Socks" Qty="2" Cost="75.50"/>
            </Line>
            <Line Line_Key="1202103301135561781564851">
                <Order Status="113353" Status_Desc="Included In Shipment" Order_Key="20210330113201178156395" Order_Num="22268086" Type="RET" />
                <Item Id="I3" Desc="Writing Pad" Qty="3" Cost="120"/>
            </Line>
        </Lines>
        <Organization>Some Org Name</Organization>
    </Shipment>
</MyShipments>
```

### JSON Schema Guidelines

1. As evident from the above example, this application/library extensively use [XPaths](https://www.w3schools.com/xml/xpath_intro.asp) for identifying the XML elements/attributes/text-nodes.

2. Anything in the schema that is not defined as XPaths, are treated as constants and are rendered into the output JSON as-is.

3. The primary rule of defining a JSON schema is to map the fields to XML attributes/text-nodes/constants are per the requirement.

4. Multiple case-sensitive keywords are used for defining the JSON schema.

5. **Working with JSON Array**:
   
   - Typically, it is expected that the repeated elements in an XML are converted into JSON Arrays.
   
   - For example, the element `Line` is a repeated element in the [above example](#xml-example).
   
   - And that, when converted to JSON should look like:
     
     ```json5
     {
         "Shipment": {
             "Lines": {
                 "Line": [{
                     ...
                 },{
                     ...
                 }]
             }
         }
     }
     ```
   
   - Note that in the JSON example above, the `Line` field is an array.
   
   - A special key field and another separate keyword has been introduced to achieve this during the conversion process.

#### Keywords

1. <a name="xpath"></a>**XPATH**:
   
   - The *XPATH* keyword is used to map a JSON field to an XML entity.
   
   - The entity in context can be an attribute (Lines: [1/2/3](#json-example)), an element (Line: [22](#json-example)) or a text-node (Line: [16](#json-example)).
   
   - When an XML element is mapped to JSON field, the whole element is set as the value in an encoded string format (with UTF-8 support).
   
   - The *XPATH* can be used to define item in array with the JSON (Line: [21](#json-example))
   
   - <u>Syntax</u>:
     
     ```json
     {
         "key": "XPATH(<COMPLETE_XPATH_EXPRESSION)"
     }
     ```

2. <a name="value"></a>**VALUE**:
   
   - For and XPath expression that is implied for an XML attribute, the *VALUE* keyword will yield the exact same result as **XPATH**.
   
   - Additionally, the *VALUE* keyword can be used for mapping the value of the XML element to a JSON field i.e. the text-nodes.
   
   - In the above [example](#xml-example), the `Organization` element has a text value which is "Some Org Name" which is mapped in Line: [18](#json-example) in the JSON schema.
   
   - <u>Syntax</u>:
     
     ```json
     {
        "key": "VALUE(<COMPLETE_XPATH_EXPRESSION>)"
     }
     ```

3. **recurrent_path**:
   
   - The *recurrent_path* is special field that can be used in the JSON schema definition when an array is required in the converted JSON.
   
   - In the above example, line: [5](#json-example) (i.e. the `LineItems` field) is a JSON array, as denoted by the square brackets.
   
   - This special field defines the path of the repeated XML element in the input XML.
   
   - For having an array in the converted JSON, the *recurrent_path* field must be present. Otherwise, it will be treated as a JSON object instead of an array.
   
   - In the above example, line: [6](#json-example) defines the path of the repeated element in the input XML, which in this case is the `Line` element.
   
   - The number of items in the final JSON array would be the same as the number of times, the element in the input XML has been repeated.
   
   - The *recurrent_path* field will **not** appear in the final converted JSON.
   
   - Syntax:
     
     ```json
     {
         "Field1": [{
             "recurrent_path": "<XPATH of the repeated element>"
         }]
     }
     ```

4. **RECUR_ELEM**:
   
   - The *RECUR_ELEM* is yet another keyword that is used to identify and map values from the XML entities to the JSON fields, specifically in the context of arrays.
   
   - This defines the path of the XML element/attribute that comes in after the repeated element (defined against the *recurrent_path* field).
   
   - In the above [example](#xml-example), the element `Item` and the corresponding attribute `Id` appears for every repeated `Line` element.
     
     - And as shown in Line: [7](#json-example), the field `Identifier` is mapped to this `Id` attribute using the *RECUR_ELEM* keyword.
     
     - For a particular field inside the final array, while the *recurrent_path* will give the first part of the XPath, the *RECUR_ELEM* will give the later part.
     
     - Combining together, the final XPath will point to the actual attribute/element in the input XML.
   
   **Note**: It is not mandatory that every field inside the array must be mapped to a repeated attribute/element in the input XML. The [XPATH](#xpath) and [VALUE](#value) keywords or any constant value can also be used in tandem with the *RECUR_ELEM* keyword (Line: [12,13,14,15,16](#json-example)). However, in the final converted JSON, the non-*RECUR_ELEM* values will appear for each item in the array (see: [result](#result)).

#### DataTypes

1. As of now, the following datatypes are supported by the conversion logic:
   
   | DataType | Keyword |
   |:--------:|:-------:|
   | Integer  | INT     |
   | Double   | DBL     |
   | Boolean  | BOOL    |
   | String   | ---     |

2. Any field which is not marked for Integer, Double or Boolean is considered to be a String.

3. <u>Syntax</u>:
   
   ```json
   {
       "key": "VALUE(<COMPLETE_XPATH_EXPRESSION>):<INT|DBL|BOOL>"
   }
   ```

4. <u>Example</u>: Line: [](#example) would give the JSON result as:
   
   ```json
   {
       "Quantity": 10
   }
   ```
   
   instead of,
   
   ```json
   {
       "Quantity": "10"
   }
   ```

### <a name="result"></a>Converted JSON Output

1. For the above given example, wherein JSON schema is [this](#json-example) and input XML is [this](#xml-example), the resultant converted JSON is:
   
   ```json
   {
       "ShipmentIdentifier": "1202103301135561781564861",
       "Type": "COD",
       "LineItems": [
           {
               "Status": "113350",
               "Field4": 528630,
               "Field2": 1000,
               "Identifier": "I1",
               "Description": "Shoes",
               "Field3": "528630",
               "Price": 150.99,
               "Quantity": 1,
               "Field1": "CONSTANT1"
           },
           {
               "Status": "113351",
               "Field4": 528630,
               "Field2": 1000,
               "Identifier": "I2",
               "Description": "Socks",
               "Field3": "528630",
               "Price": 75.5,
               "Quantity": 2,
               "Field1": "CONSTANT1"
           },
           {
               "Status": "113353",
               "Field4": 528630,
               "Field2": 1000,
               "Identifier": "I3",
               "Description": "Writing Pad",
               "Field3": "528630",
               "Price": 120,
               "Quantity": 3,
               "Field1": "CONSTANT1"
           }
       ],
       "Temp": [
           "Some Org Name",
           1234,
           "ABCD",
           "22268086"
       ],
       "OrderTempInfo": "<Order Order_Key="20210330113201178156395" Order_Num="22268086" Status="113350" Status_Desc="Included In Shipment" Type="COD_SHP"/>",
       "OrderNo": "22268086",
       "Carrier": "XYZ-ABCD Logistics"
   }
   ```

2. If the JSON schema is not provided, the resulting JSON from the default conversion logic is:
   
   ```json
   {
     "MyShipments": {
       "Shipment": {
         "Shipment_Key": 1.2021033011355618e+24,
         "Organization": "Some Org Name",
         "Number": 528630,
         "Lines": {
           "Line": [
             {
               "Order": {
                 "Status": 113350,
                 "Order_Key": 2.021033011320118e+22,
                 "Type": "COD_SHP",
                 "Order_Num": 22268086,
                 "Status_Desc": "Included In Shipment"
               },
               "Item": {
                 "Desc": "Shoes",
                 "Qty": 1,
                 "Id": "I1",
                 "Cost": 150.99
               },
               "Line_Key": 1.2021033011355618e+24
             },
             {
               "Order": {
                 "Status": 113351,
                 "Order_Key": 2.021033011320118e+22,
                 "Type": "COD_SHP",
                 "Order_Num": 22268086,
                 "Status_Desc": "Included In Shipment"
               },
               "Item": {
                 "Desc": "Socks",
                 "Qty": 2,
                 "Id": "I2",
                 "Cost": 75.5
               },
               "Line_Key": 1.2021033011355618e+24
             },
             {
               "Order": {
                 "Status": 113353,
                 "Order_Key": 2.021033011320118e+22,
                 "Type": "COD_SHP",
                 "Order_Num": 22268086,
                 "Status_Desc": "Included In Shipment"
               },
               "Item": {
                 "Desc": "Writing Pad",
                 "Qty": 3,
                 "Id": "I3",
                 "Cost": 120
               },
               "Line_Key": 1.2021033011355618e+24
             }
           ]
         }
       }
     }
   }
   ```

## Limitation(s)

- Currently, in the output JSON, an array at the very starting of the JSON (as shown below) is not supported.

```json
[
    {
        "color": "red",
        "value": 10
    },{
        "color": "blue",
        "value": 11
    },{
        "color": "green",
        "value": 12
    },
] 
```

## Scope of Improvement

- Support for simple String operations like CONCAT, SUBSTRING, LENGTH, UPPER, LOWER etc.

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) for more details on how to contribute to this project.

## License

This project is licensed under the [Apache License 2.0](LICENSE).

## Support

If you encounter any issues or have questions, please open an issue in this repository or contact the maintainers directly.

## Changelog

### Version 0.0.1-SNAPSHOT

- Initial release of XML to JSON Converter.
- Added support for CLI usage and library integration.
- Implemented schema-based XML to JSON conversion.
