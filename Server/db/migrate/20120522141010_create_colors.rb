class CreateColors < ActiveRecord::Migration
  def change
    create_table :colors do |t|
      t.string :hex
      t.references :user

      t.timestamps
    end
    add_index :colors, :user_id
  end
end
